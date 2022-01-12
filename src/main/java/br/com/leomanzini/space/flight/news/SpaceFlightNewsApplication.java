package br.com.leomanzini.space.flight.news;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Space Flight News API", version = "1.0.0", description = "API to manage articles about space"))
public class SpaceFlightNewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpaceFlightNewsApplication.class, args);
	}
}
