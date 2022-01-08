package br.com.leomanzini.space.flights.batch;

import br.com.leomanzini.space.flights.batch.service.ArticleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpaceFlightsBatchApplication implements CommandLineRunner {

	@Autowired
	private ArticleService articleService;

	public static void main(String[] args) {
		SpringApplication.run(SpaceFlightsBatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		articleService.checkApiArticlesCount();
	}
}
