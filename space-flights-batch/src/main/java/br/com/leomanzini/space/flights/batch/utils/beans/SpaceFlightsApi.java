package br.com.leomanzini.space.flights.batch.utils.beans;

import br.com.leomanzini.space.flights.batch.dto.ArticlesDTO;
import br.com.leomanzini.space.flights.batch.utils.enums.SystemCodes;
import br.com.leomanzini.space.flights.batch.utils.enums.SystemMessages;
import br.com.leomanzini.space.flights.batch.exceptions.APIException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class SpaceFlightsApi {

    @Value("${space.flights.api.context}")
    private String applicationContext;
    @Value("${space.flights.api.articles.count}")
    private String countArticles;
    @Value("${space.flights.api.all.articles}")
    private String allArticles;
    @Value("${space.flights.api.articles.by.id}")
    private String articleById;

    public Integer getSpaceFlightsArticlesCount() throws APIException {
        try {
            String responseJson = callApi(applicationContext + countArticles);
            Gson gson = new Gson();
            return gson.fromJson(responseJson, Integer.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new APIException(e.getMessage());
        } catch (APIException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(SystemMessages.NOT_FOUND_ERROR.getMessage());
        }
    }

    public ArticlesDTO getSpaceFlightsArticlesById(Long articleId) throws APIException {
        try {
            String responseJson = callApi(applicationContext +
                    articleById.replace("x", articleId.toString()));
            Gson gson = new Gson();
            return gson.fromJson(responseJson, ArticlesDTO.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new APIException(e.getMessage());
        } catch (APIException e) {
            e.printStackTrace();
            return new ArticlesDTO();
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(e.getMessage());
        }
    }

    public List<ArticlesDTO> getSpaceFlightsArticles() throws APIException {
        try {
            String responseJson = callApi(applicationContext + allArticles);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<ArticlesDTO>>() {
            }.getType();
            return gson.fromJson(responseJson, listType);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new APIException(e.getMessage());
        } catch (APIException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(e.getMessage());
        }
    }

    private String callApi(String applicationContext) throws Exception {
        URL apiUrl = new URL(applicationContext);
        HttpURLConnection apiConnection = (HttpURLConnection) apiUrl.openConnection();

        if (apiConnection.getResponseCode() != SystemCodes.SUCCESS.getCode()) {
            throw new APIException(SystemMessages.HTTP_ERROR.getMessage() + apiConnection.getResponseCode());
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
}
