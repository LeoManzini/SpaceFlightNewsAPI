package br.com.leomanzini.space.flights.batch.utils.beans;

import br.com.leomanzini.space.flights.batch.model.Article;
import br.com.leomanzini.space.flights.batch.model.Events;
import br.com.leomanzini.space.flights.batch.model.Launches;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FilesWriter {

    @Autowired
    private BufferedWriter bufferedWriter;

    public void writeHistoricalArticleInsertionFile(List<Article> articlesToInsert) {
        writeArticles(articlesToInsert);
    }

    private void writeArticles (List<Article> articlesToInsert) {
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
            String articleInsert = articleToInsertSyntax(article);
            bufferedWriter.write(articleInsert);
            bufferedWriter.newLine();
            if (!article.getLaunches().isEmpty()) {
                String launchesInsert = launchesToInsertSyntax(article.getLaunches());
                bufferedWriter.write(launchesInsert);
                bufferedWriter.newLine();
                String launchesArticleInsert = launchesArticleToInsertSyntax();
                bufferedWriter.write(launchesArticleInsert);
                bufferedWriter.newLine();
            }
            if (!article.getEvents().isEmpty()) {
                String eventsInsert = eventsToInsertSyntax(article.getEvents());
                bufferedWriter.write(eventsInsert);
                bufferedWriter.newLine();
                String eventsArticleInsert = eventsArticleToInsertSyntax();
                bufferedWriter.write(eventsArticleInsert);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Lançar excecao personalizada");
        }
    }

    private String articleToInsertSyntax (Article article) {
        return "INSERT INTO article "
                + "(id, featured, image_url, news_site, published_at, summary, title, url) "
                + "VALUES (" + article.getId() + ", " + article.getFeatured() + ", '" + article.getImageUrl()
                + "', '" + article.getNewsSite() + "', '" + article.getPublishedAt() + "', '" + article.getSummary()
                + "', '" + article.getTitle() + "', '" + article.getUrl() + "');";
    }

    private String launchesToInsertSyntax(List<Launches> launches) {
        return "";
    }

    private String launchesArticleToInsertSyntax() {
        return "";
    }

    private String eventsToInsertSyntax(List<Events> events) {
        return "";
    }

    private String eventsArticleToInsertSyntax() {
        return "";
    }
}
