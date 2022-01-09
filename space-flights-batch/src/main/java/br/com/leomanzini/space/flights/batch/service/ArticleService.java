package br.com.leomanzini.space.flights.batch.service;

import br.com.leomanzini.space.flights.batch.exceptions.ArticleException;
import br.com.leomanzini.space.flights.batch.exceptions.PersistArticleListException;
import br.com.leomanzini.space.flights.batch.exceptions.RegisterNotFoundException;
import br.com.leomanzini.space.flights.batch.exceptions.UpdateRoutineException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

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

    /*
    Criado banco de dados remoto, subir aplicação e configurar o mesmo
    postgres://hiqjjdrvhiwboc:9621d87429585c150358346dea0fd68916ea47c4f3bd21df2a6a74f895764c9c@ec2-3-231-253-230.compute-1.amazonaws.com:5432/d6pl91qj1um1rv
     */
    // TODO montar rotina de verificacao se a API recebeu novos artigos e persistir os mesmos no banco,
    //  usar tabela a parte para guardar os dados de registros inseridos e um campo na tabela Articles para saber se foi inserido por user ou API
    // TODO adicionar disparos de emails com relatorios de execucoes da rotina, caso de certo ou nao

    public void executeDatabaseUpdateRoutine() throws UpdateRoutineException {
        try {
            Integer countApiArticles = apiMethods.getSpaceFlightsArticlesCount();
            ArticleControl databaseArticleControl = articleControlRepository.findById(SystemCodes.ARTICLES_CONTROL_ID.getCode()).orElseThrow(() ->
                    new RegisterNotFoundException(SystemMessages.DATABASE_NOT_FOUND.getMessage()));

            if (countApiArticles > databaseArticleControl.getArticleCount()) {
                Long databaseLastId = databaseArticleControl.getLastArticleId();
                List<Article> articlesToPersist = new ArrayList<>();

                while (true) {
                    Article newArticle = mapper.dtoToEntity(apiMethods.getSpaceFlightsArticlesById(++databaseLastId));
                    if (newArticle.getId() == null) {
                        break;
                    } else {
                        articlesToPersist.add(newArticle);
                    }
                }
                persistArticleList(articlesToPersist);
                updateArticleControl(databaseArticleControl, countApiArticles, --databaseLastId);
                // criar metodo para envio de relatorio por email sendInsertionReport();
            } // else { envia um relatorio de base de dados estava atualizada, sem artigos novos na api
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateRoutineException(SystemMessages.UPDATE_ROUTINE_ERROR.getMessage());
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
            e.printStackTrace();
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
