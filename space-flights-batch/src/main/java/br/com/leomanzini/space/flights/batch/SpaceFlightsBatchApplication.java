package br.com.leomanzini.space.flights.batch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpaceFlightsBatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpaceFlightsBatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
	}
}
