package br.com.leomanzini.space.flights.batch.config;

import br.com.leomanzini.space.flights.batch.beans.SpaceFlightsApiMethods;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpaceFlightsApiConfig {

    @Bean
    public SpaceFlightsApiMethods spaceFlightsApiMethods() {
        return new SpaceFlightsApiMethods();
    }
}
