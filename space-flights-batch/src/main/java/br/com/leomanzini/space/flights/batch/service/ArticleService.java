package br.com.leomanzini.space.flights.batch.service;

import br.com.leomanzini.space.flights.batch.exceptions.*;
import br.com.leomanzini.space.flights.batch.model.Article;
import br.com.leomanzini.space.flights.batch.model.ArticleControl;
import br.com.leomanzini.space.flights.batch.repository.ArticleControlRepository;
import br.com.leomanzini.space.flights.batch.repository.ArticleRepository;
import br.com.leomanzini.space.flights.batch.repository.EventsRepository;
import br.com.leomanzini.space.flights.batch.repository.LaunchesRepository;
import br.com.leomanzini.space.flights.batch.utils.beans.FilesWriter;
import br.com.leomanzini.space.flights.batch.utils.beans.ModelMapperMethods;
import br.com.leomanzini.space.flights.batch.utils.beans.SpaceFlightsApi;
import br.com.leomanzini.space.flights.batch.utils.enums.SystemCodes;
import br.com.leomanzini.space.flights.batch.utils.enums.SystemMessages;
import org.apache.logging.log4j.LogManager;
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
    private ModelMapperMethods mapper;
    @Autowired
    private SpaceFlightsApi apiMethods;
    @Autowired
    private FilesWriter filesWriter;

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private EventsRepository eventsRepository;
    @Autowired
    private LaunchesRepository launchesRepository;
    @Autowired
    private ArticleControlRepository articleControlRepository;

    // TODO montar rotina de verificacao se a API recebeu novos artigos e persistir os mesmos no banco,
    //  usar tabela a parte para guardar os dados de registros inseridos e um campo na tabela Articles para saber se foi inserido por user ou API
    // TODO adicionar disparos de emails com relatorios de execucoes da rotina, caso de certo ou nao

    public void executeDatabaseUpdateRoutine() throws UpdateRoutineException {
        Integer countApiArticles = null;
        ArticleControl databaseArticleControl = null;
        Long databaseLastId = null;

        try {
            countApiArticles = apiMethods.getSpaceFlightsArticlesCount();
            log.info("API articles count: " + countApiArticles);

            databaseArticleControl = articleControlRepository.findById(SystemCodes.ARTICLES_CONTROL_ID.getCode()).orElseThrow(() ->
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
            updateArticleControl(databaseArticleControl, countApiArticles, --databaseLastId);
        }
    }

    public void executeHistoricalInsertDocumentWrite() throws HistoricalRoutineException, RegisterNotFoundException {
        ArticleControl databaseArticleControl = null;
        Long articlesIdCounter = null;
        try {
            log.info("Starting database historical document write");

            databaseArticleControl = articleControlRepository.findById(SystemCodes.ARTICLES_CONTROL_ID.getCode()).orElseThrow(() ->
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
            updateArticleControl(databaseArticleControl, 1, articlesIdCounter);
        }
    }

    private void persistArticleList(List<Article> receivedObject) throws PersistArticleListException {
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
        } else {
            throw new ArticleException(SystemMessages.ARTICLE_FOUND_AT_DATABASE.getMessage());
        }
    }

    private void updateArticleControl(ArticleControl databaseArticleControl, Integer countApiArticles, Long lastId) {
        databaseArticleControl.setArticleCount(countApiArticles.longValue());
        databaseArticleControl.setLastArticleId(lastId);

        articleControlRepository.saveAndFlush(databaseArticleControl);
    }
}
