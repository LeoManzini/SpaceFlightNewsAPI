package br.com.leomanzini.space.flights.batch.service;

import br.com.leomanzini.space.flights.batch.dto.ArticlesResponseDTO;
import br.com.leomanzini.space.flights.batch.model.Article;
import br.com.leomanzini.space.flights.batch.repository.ArticleRepository;
import br.com.leomanzini.space.flights.batch.repository.EventsRepository;
import br.com.leomanzini.space.flights.batch.repository.LaunchesRepository;
import br.com.leomanzini.space.flights.batch.utils.ResponseCodes;
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

    @Value("${space.flights.api.context}")
    private String applicationContext;
    @Value("${space.flights.api.articles.count}")
    private String countArticles;
    @Value("${space.flights.api.all.articles}")
    private String allArticles;

    // TODO criar banco de dados remoto
    // TODO montar documento sql com a inserção histórica
    // TODO montar rotina de insercao e verificacao de dados existentes na base
    // TODO montar rotina de verificacao se a API recebeu novos artigos e persistir os mesmos no banco,
    //  usar tabela a parte para guardar os dados de registros inseridos e um campo na tabela Articles para saber se foi inserido por user ou API
    // TODO adicionar disparos de emails com relatorios de execucoes da rotina, caso de certo ou nao
    public void insertNewArticle() {
        try {
            List<ArticlesResponseDTO> receivedDtoObject = getSpaceFlightsArticles();
            receivedDtoObject.forEach(item -> {
                Article insertObject = dtoToEntity(item);
                System.out.println(insertObject);

//              eventsRepository.saveAll(insertObject.getEvents());
//              launchesRepository.saveAll(insertObject.getLaunches());
//              articleRepository.save(insertObject);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkApiArticlesCount() {
        try {
            System.out.println("Today API total articles: " + getSpaceFlightsArticlesCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer getSpaceFlightsArticlesCount() throws Exception {
        try {
            String responseJson = callApi(applicationContext+allArticles);
            Gson gson = new Gson();
            return gson.fromJson(responseJson, Integer.class);
        }  catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    private List<ArticlesResponseDTO> getSpaceFlightsArticles() throws Exception {
        try {
            String responseJson = callApi(applicationContext+allArticles);
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

    private String callApi (String applicationContext) throws IOException {
        URL apiUrl = new URL(applicationContext+allArticles);
        HttpURLConnection apiConnection = (HttpURLConnection) apiUrl.openConnection();

        if (apiConnection.getResponseCode() != ResponseCodes.SUCCESS.getResponseCode()) {
            throw new RuntimeException(SystemMessages.HTTP_ERROR.getMessage() + apiConnection.getResponseCode());
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
