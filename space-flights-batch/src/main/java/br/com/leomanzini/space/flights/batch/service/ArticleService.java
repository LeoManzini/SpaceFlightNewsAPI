package br.com.leomanzini.space.flights.batch.service;

import br.com.leomanzini.space.flights.batch.dto.ArticlesResponseDTO;
import br.com.leomanzini.space.flights.batch.model.Article;
import br.com.leomanzini.space.flights.batch.model.Events;
import br.com.leomanzini.space.flights.batch.model.Launches;
import br.com.leomanzini.space.flights.batch.repository.ArticleRepository;
import br.com.leomanzini.space.flights.batch.repository.EventsRepository;
import br.com.leomanzini.space.flights.batch.repository.LaunchesRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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

    public void insertNewArticle() {
        try {
            ArticlesResponseDTO receivedDtoObject = getSpaceFlightsArticles();
            Article insertObject = new Article();

            insertObject.setId(receivedDtoObject.getId());
            insertObject.setFeatured(receivedDtoObject.getFeatured());
            insertObject.setTitle(receivedDtoObject.getTitle());
            insertObject.setUrl(receivedDtoObject.getUrl());
            insertObject.setImageUrl(receivedDtoObject.getImageUrl());
            insertObject.setNewsSite(receivedDtoObject.getNewsSite());
            insertObject.setSummary(receivedDtoObject.getSummary());
            insertObject.setPublishedAt(receivedDtoObject.getPublishedAt());

            insertObject.setLaunches(new ArrayList<>());
            insertObject.setEvents(new ArrayList<>());

            receivedDtoObject.getLaunches().forEach(item -> {
                insertObject.getLaunches().add(Launches.builder().id(item.getId()).provider(item.getProvider()).build());
            });

            receivedDtoObject.getEvents().forEach(item -> {
                insertObject.getEvents().add(Events.builder().id(item.getId()).provider(item.getProvider()).build());
            });

            eventsRepository.saveAll(insertObject.getEvents());
            launchesRepository.saveAll(insertObject.getLaunches());
            articleRepository.save(insertObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArticlesResponseDTO getSpaceFlightsArticles() throws Exception {
        try {
            URL apiUrl = new URL(applicationContext+allArticles);
            HttpURLConnection apiConnection = (HttpURLConnection) apiUrl.openConnection();

            if(apiConnection.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code: " + apiConnection.getResponseCode());
            }

            BufferedReader apiResponse = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
            String responseJson = jsonIntoString(apiResponse);

            Gson gson = new Gson();
            return gson.fromJson(responseJson, ArticlesResponseDTO.class);

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
