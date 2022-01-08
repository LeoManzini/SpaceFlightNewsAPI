package br.com.leomanzini.space.flights.batch;

import br.com.leomanzini.space.flights.batch.model.Article;
import br.com.leomanzini.space.flights.batch.model.Events;
import br.com.leomanzini.space.flights.batch.model.Launches;
import br.com.leomanzini.space.flights.batch.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class SpaceFlightsBatchApplication implements CommandLineRunner {

	@Autowired
	private ArticleService articleService;

	public static void main(String[] args) {
		SpringApplication.run(SpaceFlightsBatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Article article = new Article();
		article.setFeatured(true);
		article.setTitle("Leonardo");
		article.setUrl("leonardo.com");
		article.setImageUrl("www.leonardo.com");
		article.setNewsSite("Google");
		article.setSummary("Sumario");
		article.setPublishedAt("Hoje");
		article.setLaunches(new ArrayList<>());
		article.setEvents(new ArrayList<>());
		article.getLaunches().add(Launches.builder().id("2000").provider("Eu mesmo").build());
		article.getEvents().add(Events.builder().id(1L).provider("Eu mesmo").build());

		System.out.println(article);

		articleService.insertNewArticle(article);
	}
}
