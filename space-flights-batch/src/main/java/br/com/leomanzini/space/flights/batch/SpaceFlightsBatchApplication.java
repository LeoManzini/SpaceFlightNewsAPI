package br.com.leomanzini.space.flights.batch;

import br.com.leomanzini.space.flights.batch.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpaceFlightsBatchApplication implements CommandLineRunner {

	@Autowired
	private final ArticleService articleService;

	public SpaceFlightsBatchApplication(ArticleService articleService) {
		this.articleService = articleService;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpaceFlightsBatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			int count = 0;
			while (count < 2000) {
				count++;
				articleService.executeHistoricalInsertDocumentWrite();
			}
		} catch (Exception e) {
			SpringApplication.run(SpaceFlightsBatchApplication.class, args);
		}
	}
}
