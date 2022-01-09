package br.com.leomanzini.space.flights.batch.utils.beans;

import br.com.leomanzini.space.flights.batch.exceptions.WriteException;
import br.com.leomanzini.space.flights.batch.model.Article;
import br.com.leomanzini.space.flights.batch.model.Events;
import br.com.leomanzini.space.flights.batch.model.Launches;
import br.com.leomanzini.space.flights.batch.utils.enums.SystemMessages;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilesWriter {

    @Autowired
    private BufferedWriter bufferedWriter;

    public void writeHistoricalArticleInsertionFile(List<Article> articlesToInsert) {
        writeArticles(articlesToInsert);
    }

    private void writeArticles(List<Article> articlesToInsert) {
        articlesToInsert.forEach(article -> {
            try {
                writeArticle(article);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void writeArticle(Article article) throws Exception {
        try {
            write(articleToInsertSyntax(article));
            if (!article.getLaunches().isEmpty()) {
                writeLaunches(article.getLaunches());
                writeLaunchesArticles(article);
            }
            if (!article.getEvents().isEmpty()) {
                writeEvents(article.getEvents());
                writeEventsArticles(article);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new WriteException(SystemMessages.WRITE_ERROR.getMessage());
        }
    }

    private void writeLaunches(List<Launches> launches) throws IOException {
        List<String> launchesInsert = launchesToInsertSyntax(launches);
        write(launchesInsert);
    }

    private void writeLaunchesArticles(Article article) throws IOException {
        List<String> launchesArticlesInsert = launchesArticleToInsertSyntax(article.getId(),
                getLaunchesIdList(article.getLaunches()));
        write(launchesArticlesInsert);
    }

    private void writeEvents(List<Events> events) throws IOException {
        List<String> eventsInsert = eventsToInsertSyntax(events);
        write(eventsInsert);
    }

    private void writeEventsArticles(Article article) throws IOException {
        List<String> eventsArticlesInsert = eventsArticleToInsertSyntax(article.getId(),
                getEventsIdList(article.getEvents()));
        write(eventsArticlesInsert);
    }

    private String articleToInsertSyntax (Article article) {
        return "INSERT INTO article "
                + "(id, featured, image_url, news_site, published_at, summary, title, url) "
                + "VALUES (" + article.getId() + ", " + article.getFeatured() + ", '" + article.getImageUrl()
                + "', '" + article.getNewsSite() + "', '" + article.getPublishedAt() + "', '" + article.getSummary()
                + "', '" + article.getTitle() + "', '" + article.getUrl() + "');";
    }

    private List<String> launchesToInsertSyntax(List<Launches> launches) {
        List<String> launchesToWrite = new ArrayList<>();
        launches.forEach(launch -> launchesToWrite.add("INSERT INTO launches "
                + "(id, provider) "
                + "VALUES ('" + launch.getId() + "', '" + launch.getProvider() + "');"));
        return launchesToWrite;
    }

    private List<String> launchesArticleToInsertSyntax(Long articleId, List<String> launchesId) {
        List<String> launchesArticlesToWrite = new ArrayList<>();
        launchesId.forEach(launchId -> launchesArticlesToWrite.add("INSERT INTO article_launches "
                + "(article_id, launches_id) "
                + "VALUES (" + articleId + ", '" + launchId + "');"));
        return launchesArticlesToWrite;
    }

    private List<String> eventsToInsertSyntax(List<Events> events) {
        List<String> eventsToWrite = new ArrayList<>();
        events.forEach(event -> eventsToWrite.add("INSERT INTO events "
                + "(id, provider) "
                + "VALUES (" + event.getId() + ", '" + event.getProvider() + "');"));
        return eventsToWrite;
    }

    private List<String> eventsArticleToInsertSyntax(Long articleId, List<Long> eventsId) {
        List<String> eventsArticleToWrite = new ArrayList<>();
        eventsId.forEach(eventId -> eventsArticleToWrite.add("INSERT INTO article_events "
                + "(article_id, events_id) "
                + "VALUES (" + articleId + ", " + eventId + ");"));
        return eventsArticleToWrite;
    }

    private List<String> getLaunchesIdList(List<Launches> launches) {
        List<String> launchesId = new ArrayList<>();
        launches.forEach(launch -> launchesId.add(launch.getId()));
        return launchesId;
    }

    private List<Long> getEventsIdList(List<Events> events) {
        List<Long> eventsId = new ArrayList<>();
        events.forEach(event -> eventsId.add(event.getId()));
        return eventsId;
    }

    private void write(String stringToWrite) {
        try {
            bufferedWriter.write(stringToWrite);
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(List<String> listToWrite) {
        listToWrite.forEach(listItem -> {
            try {
                bufferedWriter.write(listItem);
                bufferedWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
