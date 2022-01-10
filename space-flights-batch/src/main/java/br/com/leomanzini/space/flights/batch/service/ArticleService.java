package br.com.leomanzini.space.flights.batch.service;

import br.com.leomanzini.space.flights.batch.exceptions.*;
import br.com.leomanzini.space.flights.batch.model.Article;
import br.com.leomanzini.space.flights.batch.model.ArticleControl;
import br.com.leomanzini.space.flights.batch.repository.ArticleControlCrudRepository;
import br.com.leomanzini.space.flights.batch.repository.ArticleRepository;
import br.com.leomanzini.space.flights.batch.repository.EventsRepository;
import br.com.leomanzini.space.flights.batch.repository.LaunchesRepository;
import br.com.leomanzini.space.flights.batch.utils.beans.FilesWriter;
import br.com.leomanzini.space.flights.batch.utils.beans.ModelMapperMethods;
import br.com.leomanzini.space.flights.batch.utils.beans.SpaceFlightsApi;
import br.com.leomanzini.space.flights.batch.utils.enums.SystemCodes;
import br.com.leomanzini.space.flights.batch.utils.enums.SystemMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private final ModelMapperMethods mapper;
    @Autowired
    private final SpaceFlightsApi apiMethods;
    @Autowired
    private final FilesWriter filesWriter;

    @Autowired
    private final ArticleRepository articleRepository;
    @Autowired
    private final EventsRepository eventsRepository;
    @Autowired
    private final LaunchesRepository launchesRepository;
    @Autowired
    private final ArticleControlCrudRepository articleControlCrudRepository;

    public ArticleService(ModelMapperMethods mapper, SpaceFlightsApi apiMethods, FilesWriter filesWriter, ArticleRepository articleRepository, EventsRepository eventsRepository, LaunchesRepository launchesRepository, ArticleControlCrudRepository articleControlCrudRepository) {
        this.mapper = mapper;
        this.apiMethods = apiMethods;
        this.filesWriter = filesWriter;
        this.articleRepository = articleRepository;
        this.eventsRepository = eventsRepository;
        this.launchesRepository = launchesRepository;
        this.articleControlCrudRepository = articleControlCrudRepository;
    }

    // TODO corrigir arquivo de insert
    // TODO adicionar disparos de emails com relatorios de execucoes da rotina, caso de certo ou nao

    public void executeDatabaseUpdateRoutine() throws UpdateRoutineException {
        ArticleControl databaseArticleControl = null;
        Long databaseLastId = 0L;

        try {
            Integer countApiArticles = apiMethods.getSpaceFlightsArticlesCount();
            log.info("API articles count: " + countApiArticles);

            databaseArticleControl = articleControlCrudRepository.findById(SystemCodes.ARTICLES_CONTROL_ID.getCode()).orElseThrow(() ->
                    new RegisterNotFoundException(SystemMessages.DATABASE_NOT_FOUND.getMessage()));

            log.info("Database articles count: " + databaseArticleControl.getArticleCount());

            if (countApiArticles > databaseArticleControl.getArticleCount()) {

                log.info("Starting database update");

                databaseLastId = databaseArticleControl.getLastArticleId();
                List<Article> articlesToPersist = new ArrayList<>();

                while (true) {
                    Article newArticle = mapper.dtoToEntity(apiMethods.getSpaceFlightsArticlesById(++databaseLastId));
                    if (newArticle.getId() == null) {
                        break;
                    } else {
                        articlesToPersist.add(newArticle);
                    }
                }

                log.info("Starting data persistence");

                persistArticleList(articlesToPersist);
                // criar metodo para envio de relatorio por email sendInsertionReport();
            } // else { envia um relatorio de base de dados estava atualizada, sem artigos novos na api
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UpdateRoutineException(SystemMessages.UPDATE_ROUTINE_ERROR.getMessage());
        } finally {
            assert databaseArticleControl != null;
            updateArticleControl(databaseArticleControl, articleControlCrudRepository.apiArticlesCount(), databaseLastId);
        }
    }

    public void executeHistoricalInsertDocumentWrite() throws HistoricalRoutineException, RegisterNotFoundException {
        ArticleControl databaseArticleControl = null;
        Long articlesIdCounter = 0L;
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
        } catch (APIException e) {
            log.error(e.getMessage(), e);
            throw new HistoricalRoutineException(SystemMessages.HISTORICAL_ROUTINE_API_ERROR.getMessage());
        } catch (WriteException e) {
            log.error(e.getMessage(), e);
            throw new HistoricalRoutineException(SystemMessages.HISTORICAL_ROUTINE_WRITE_ERROR.getMessage());
        } finally {
            assert databaseArticleControl != null;
            updateArticleControl(databaseArticleControl, articleControlCrudRepository.apiArticlesCount(), articlesIdCounter);
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

    private void updateArticleControl(ArticleControl databaseArticleControl, Long countApiArticles, Long lastId) {
        databaseArticleControl.setArticleCount(countApiArticles);
        databaseArticleControl.setLastArticleId(lastId);
        articleControlCrudRepository.save(databaseArticleControl);

        log.info("Articles count " + countApiArticles.toString());
        log.info("Article control table updated successfully");
    }
}
