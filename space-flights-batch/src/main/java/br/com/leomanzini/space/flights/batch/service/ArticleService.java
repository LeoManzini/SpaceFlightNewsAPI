package br.com.leomanzini.space.flights.batch.service;

import br.com.leomanzini.space.flights.batch.dto.ArticlesResponseDTO;
import br.com.leomanzini.space.flights.batch.exceptions.*;
import br.com.leomanzini.space.flights.batch.model.Article;
import br.com.leomanzini.space.flights.batch.model.ArticleControl;
import br.com.leomanzini.space.flights.batch.repository.ArticleControlRepository;
import br.com.leomanzini.space.flights.batch.repository.ArticleRepository;
import br.com.leomanzini.space.flights.batch.repository.EventsRepository;
import br.com.leomanzini.space.flights.batch.repository.LaunchesRepository;
import br.com.leomanzini.space.flights.batch.utils.SystemCodes;
import br.com.leomanzini.space.flights.batch.utils.SystemMessages;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private EventsRepository eventsRepository;
    @Autowired
    private LaunchesRepository launchesRepository;
    @Autowired
    private ArticleControlRepository articleControlRepository;

    @Value("${space.flights.api.context}")
    private String applicationContext;
    @Value("${space.flights.api.articles.count}")
    private String countArticles;
    @Value("${space.flights.api.all.articles}")
    private String allArticles;
    @Value("${space.flights.api.articles.by.id}")
    private String articleById;

    // TODO criar banco de dados remoto
    // TODO montar documento sql com a inserção histórica
    // TODO montar rotina de insercao e verificacao de dados existentes na base
    // TODO montar rotina de verificacao se a API recebeu novos artigos e persistir os mesmos no banco,
    //  usar tabela a parte para guardar os dados de registros inseridos e um campo na tabela Articles para saber se foi inserido por user ou API
    // TODO adicionar disparos de emails com relatorios de execucoes da rotina, caso de certo ou nao

    public void executeDatabaseUpdateRoutine() throws UpdateRoutineException {
        try {
            Integer countApiArticles = getSpaceFlightsArticlesCount();
            ArticleControl databaseArticleControl = articleControlRepository.findById(SystemCodes.ARTICLES_CONTROL_ID.getCode()).orElseThrow(() ->
                    new RegisterNotFoundException(SystemMessages.DATABASE_NOT_FOUND.getMessage()));

            if (countApiArticles > databaseArticleControl.getArticleCount()) {
                Long databaseLastId = databaseArticleControl.getLastArticleId();
                List<Article> articlesToPersist = new ArrayList<>();

                while (true) {
                    Article newArticle = dtoToEntity(getSpaceFlightsArticlesById(++databaseLastId));
                    if (newArticle.getId() == null) {
                        break;
                    } else {
                        articlesToPersist.add(newArticle);
                    }
                }
                persistArticleList(articlesToPersist);
                // criar metodo para envio de relatorio por email sendInsertionReport();
            } // else { envia um relatorio de base de dados estava atualizada, sem artigos novos na api
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateRoutineException(SystemMessages.UPDATE_ROUTINE_ERROR.getMessage());
        }
    }

    private void persistArticleList(List<Article> receivedObject) throws PersistArticleListException {
        try {
            receivedObject.forEach(articleToPersist -> {
                try {
                    persistArticle(articleToPersist);
                } catch (ArticleException e) {
                    e.printStackTrace();
                }
            });
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

    private Integer getSpaceFlightsArticlesCount() throws Exception {
        try {
            String responseJson = callApi(applicationContext + countArticles);
            Gson gson = new Gson();
            return gson.fromJson(responseJson, Integer.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } catch (APINotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    private ArticlesResponseDTO getSpaceFlightsArticlesById(Long articleId) throws Exception {
        try {
            String responseJson = callApi(applicationContext +
                    articleById.replace("x", articleId.toString()));
            Gson gson = new Gson();
            return gson.fromJson(responseJson, ArticlesResponseDTO.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } catch (APINotFoundException e) {
            e.printStackTrace();
            return new ArticlesResponseDTO();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    private List<ArticlesResponseDTO> getSpaceFlightsArticles() throws Exception {
        try {
            String responseJson = callApi(applicationContext + allArticles);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<ArticlesResponseDTO>>() {
            }.getType();
            return gson.fromJson(responseJson, listType);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } catch (APINotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    private String callApi(String applicationContext) throws Exception {
        URL apiUrl = new URL(applicationContext);
        HttpURLConnection apiConnection = (HttpURLConnection) apiUrl.openConnection();

        if (apiConnection.getResponseCode() != SystemCodes.SUCCESS.getCode()) {
            throw new APINotFoundException(SystemMessages.HTTP_ERROR.getMessage() + apiConnection.getResponseCode());
        }
        BufferedReader apiResponse = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
        return jsonIntoString(apiResponse);
    }

    private String jsonIntoString(BufferedReader bufferedReader) throws IOException {
        String response = "";
        String jsonToString = "";

        while ((response = bufferedReader.readLine()) != null) {
            jsonToString += response;
        }
        return jsonToString;
    }

    private Article dtoToEntity(ArticlesResponseDTO articleDto) {
        return modelMapper.map(articleDto, Article.class);
    }

    private ArticlesResponseDTO entityToDto(Article article) {
        return modelMapper.map(article, ArticlesResponseDTO.class);
    }
}
