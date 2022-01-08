package br.com.leomanzini.space.flights.batch.service;

import br.com.leomanzini.space.flights.batch.dto.ArticlesResponseDTO;
import br.com.leomanzini.space.flights.batch.model.Article;
import br.com.leomanzini.space.flights.batch.model.Events;
import br.com.leomanzini.space.flights.batch.model.Launches;
import br.com.leomanzini.space.flights.batch.repository.ArticleRepository;
import br.com.leomanzini.space.flights.batch.repository.EventsRepository;
import br.com.leomanzini.space.flights.batch.repository.LaunchesRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private LaunchesRepository launchesRepository;

    @Value("${space.flights.api.context}")
    private String applicationContext;

    @Value("${space.flights.api.articles.count}")
    private String countArticles;

    @Value("${space.flights.api.all.articles}")
    private String allArticles;

    // TODO montar rotina de insercao e verificacao de dados existentes na base
    // TODO montar rotina de verificacao se a API recebeu novos artigos e persistir os mesmos no banco,
    //  usar tabela a parte para guardar os dados de registros inseridos e um campo na tabela Articles para saber se foi inserido por user ou API
    // TODO adicionar disparos de emails com relatorios de execucoes da rotina, caso de certo ou nao
    public void insertNewArticle() {
        try {
            List<ArticlesResponseDTO> receivedDtoObject = getSpaceFlightsArticles();
            receivedDtoObject.forEach(item -> {
                Article insertObject = new Article();

                insertObject.setId(item.getId());
                insertObject.setFeatured(item.getFeatured());
                insertObject.setTitle(item.getTitle());
                insertObject.setUrl(item.getUrl());
                insertObject.setImageUrl(item.getImageUrl());
                insertObject.setNewsSite(item.getNewsSite());
                insertObject.setSummary(item.getSummary());
                insertObject.setPublishedAt(item.getPublishedAt());

                insertObject.setLaunches(new ArrayList<>());
                insertObject.setEvents(new ArrayList<>());

                item.getLaunches().forEach(items -> {
                    insertObject.getLaunches().add(Launches.builder().id(items.getId()).provider(items.getProvider()).build());
                });

                item.getEvents().forEach(items -> {
                    insertObject.getEvents().add(Events.builder().id(items.getId()).provider(items.getProvider()).build());
                });

                eventsRepository.saveAll(insertObject.getEvents());
                launchesRepository.saveAll(insertObject.getLaunches());
                articleRepository.save(insertObject);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ArticlesResponseDTO> getSpaceFlightsArticles() throws Exception {
        try {
            URL apiUrl = new URL(applicationContext+allArticles);
            HttpURLConnection apiConnection = (HttpURLConnection) apiUrl.openConnection();

            if(apiConnection.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code: " + apiConnection.getResponseCode());
            }

            BufferedReader apiResponse = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
            String responseJson = jsonIntoString(apiResponse);

            Gson gson = new Gson();
            Type listType = new TypeToken<List<ArticlesResponseDTO>>(){}.getType();
            return gson.fromJson(responseJson, listType);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static String jsonIntoString(BufferedReader bufferedReader) throws IOException {

        String response = "";
        String jsonToString = "";

        while ((response = bufferedReader.readLine()) != null) {
            jsonToString += response;
        }
        return jsonToString;
    }
}
