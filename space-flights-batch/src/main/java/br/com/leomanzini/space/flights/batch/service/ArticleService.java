package br.com.leomanzini.space.flights.batch.service;

import br.com.leomanzini.space.flights.batch.exceptions.*;
import br.com.leomanzini.space.flights.batch.model.Article;
import br.com.leomanzini.space.flights.batch.model.ArticleControl;
import br.com.leomanzini.space.flights.batch.repository.*;
import br.com.leomanzini.space.flights.batch.utils.beans.FilesWriter;
import br.com.leomanzini.space.flights.batch.utils.beans.ModelMapperMethods;
import br.com.leomanzini.space.flights.batch.utils.beans.SpaceFlightsApi;
import br.com.leomanzini.space.flights.batch.utils.enums.SystemCodes;
import br.com.leomanzini.space.flights.batch.utils.enums.SystemMessages;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private final ModelMapperMethods mapper;
    @Autowired
    private final SpaceFlightsApi apiMethods;
    @Autowired
    private final FilesWriter filesWriter;
    @Autowired
    private final EmailService emailService;

    @Autowired
    private final ArticleRepository articleRepository;
    @Autowired
    private final EventsRepository eventsRepository;
    @Autowired
    private final LaunchesRepository launchesRepository;
    @Autowired
    private final ArticleControlRepository articleControlCrudRepository;

    public void executeDatabaseUpdateRoutine() throws UpdateRoutineException {
        Long databaseArticlesControl = 0L;
        Long databaseLastId = 0L;
        String mailMessage = "";
        try {
            Integer countApiArticles = apiMethods.getSpaceFlightsArticlesCount();
            log.info("API articles count: " + countApiArticles);

            databaseArticlesControl = (articleControlCrudRepository.apiDeletedArticlesCount() + articleControlCrudRepository.apiDeletedArticlesCount());

            log.info("Database articles count: " + databaseArticlesControl);
            ArticleControl articleControl = articleControlCrudRepository.findById(SystemCodes.ARTICLES_CONTROL_ID.getCode()).orElseThrow();
            databaseLastId = articleControl.getLastArticleId();

            if (countApiArticles > databaseArticlesControl) {
                log.info("Starting database update");
                List<Article> articlesToPersist = new ArrayList<>();

                while (true) {
                    Article newArticle = mapper.dtoToEntity(apiMethods.getSpaceFlightsArticlesById(++databaseLastId));
                    log.info("Current id " + databaseLastId);
                    if (newArticle.getId() == null) {
                        break;
                    } else {
                        articlesToPersist.add(newArticle);
                    }
                }
                log.info("Starting data persistence");
                persistArticleList(articlesToPersist);
                log.info("Sending report email to database update");
                mailMessage = SystemMessages.DATABASE_UPDATED_REPORT.getMessage();
            } else {
                mailMessage = SystemMessages.DATABASE_ALREADY_UPDATED_REPORT.getMessage();
            }
        } catch (APIException e) {
            mailMessage = SystemMessages.MAIL_API_ERROR.getMessage();
            log.error(e.getMessage(), e);
            throw new UpdateRoutineException(SystemMessages.API_GET_ARTICLE_ID.getMessage());
        } catch (PersistArticleListException e) {
            mailMessage = SystemMessages.MAIL_PERSISTENCE_ERROR.getMessage();
            log.error(e.getMessage(), e);
            throw new UpdateRoutineException(SystemMessages.INSERT_LISTS_ERROR.getMessage());
        } catch (Exception e) {
            mailMessage = SystemMessages.MAIL_UPDATE_GENERIC_ERROR.getMessage();
            log.error(e.getMessage(), e);
            throw new UpdateRoutineException(SystemMessages.UPDATE_ROUTINE_ERROR.getMessage());
        } finally {
            log.info("Sending report email");
            emailService.sendEmail(mailMessage);
            assert databaseArticlesControl != null;
            updateArticleControl(databaseArticlesControl, databaseLastId);
        }
    }

    public void executeHistoricalInsertDocumentWrite() throws HistoricalRoutineException {
        ArticleControl databaseArticleControl = null;
        Long articlesIdCounter = 0L;
        String mailMessage = "";
        try {
            log.info("Starting database historical document write");

            databaseArticleControl = articleControlCrudRepository.findById(SystemCodes.ARTICLES_CONTROL_ID.getCode()).orElseThrow(() ->
                    new RegisterNotFoundException(SystemMessages.DATABASE_NOT_FOUND.getMessage()));
            articlesIdCounter = databaseArticleControl.getLastArticleId();
            List<Article> articlesToWrite = new ArrayList<>();

            log.info("Retrieving data from API, start id: " + articlesIdCounter);

            while (true) {
                Article articleToWrite = mapper.dtoToEntity(apiMethods.getSpaceFlightsArticlesById(++articlesIdCounter));

                log.info("Current article id: " + articlesIdCounter);

                if (articleToWrite.getId() == null) {
                    log.info("Current article id " + articlesIdCounter + " = null");
                    break;
                } else {
                    articlesToWrite.add(articleToWrite);
                }
            }
            log.info("Starting to write data to sql file");
            filesWriter.writeHistoricalArticleInsertionFile(articlesToWrite);
            mailMessage = SystemMessages.EMAIL_HISTORICAL_REPORT.getMessage();
        } catch (APIException e) {
            mailMessage = SystemMessages.MAIL_API_ERROR.getMessage();
            log.error(e.getMessage(), e);
            throw new HistoricalRoutineException(SystemMessages.HISTORICAL_ROUTINE_API_ERROR.getMessage());
        } catch (WriteException e) {
            mailMessage = SystemMessages.MAIL_WRITE_ERROR.getMessage();
            log.error(e.getMessage(), e);
            throw new HistoricalRoutineException(SystemMessages.HISTORICAL_ROUTINE_WRITE_ERROR.getMessage());
        } catch (Exception e) {
            mailMessage = SystemMessages.MAIL_HISTORICAL_GENERIC_ERROR.getMessage();
            log.error(e.getMessage(), e);
            throw new HistoricalRoutineException(SystemMessages.HISTORICAL_ROUTINE_WRITE_ERROR.getMessage());
        } finally {
            log.info("Sending report email");
            emailService.sendEmail(mailMessage);
            assert databaseArticleControl != null;
            updateArticleControl(articleControlCrudRepository.apiArticlesCount(), articlesIdCounter);
        }
    }

    private void persistArticleList(List<Article> receivedObject) throws PersistArticleListException {
        log.info("Starting Articles list persistence");
        try {
            if (!receivedObject.isEmpty()) {
                receivedObject.forEach(articleToPersist -> {
                    try {
                        persistArticle(articleToPersist);
                    } catch (ArticleException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new PersistArticleListException(SystemMessages.PERSIST_ARTICLE_LIST_ERROR.getMessage());
        }
    }

    private void persistArticle(Article articleToPersist) throws ArticleException {
        if (articleRepository.findById(articleToPersist.getId()).isEmpty()) {
            log.info("Current article id: " + articleToPersist.getId());
            articleToPersist.getLaunches().forEach(launches -> {
                if (launchesRepository.findById(launches.getId()).isEmpty()) {
                    launchesRepository.save(launches);
                }
            });
            articleToPersist.getEvents().forEach(events -> {
                if (eventsRepository.findById(events.getId()).isEmpty()) {
                    eventsRepository.save(events);
                }
            });
            articleRepository.save(articleToPersist);
            log.info("Article persisted successfully");
        } else {
            throw new ArticleException(SystemMessages.ARTICLE_FOUND_AT_DATABASE.getMessage());
        }
    }

    private void updateArticleControl(Long countApiArticles, Long lastId) {
        articleControlCrudRepository.save(ArticleControl.builder()
                .id(SystemCodes.ARTICLES_CONTROL_ID.getCode())
                .articleCount(countApiArticles)
                .lastArticleId(lastId).build());

        log.info("Articles count " + countApiArticles.toString());
        log.info("Article control table updated successfully");
    }
}
