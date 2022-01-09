package br.com.leomanzini.space.flights.batch.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpaceFlightsApiConfig {

    @Bean
    public SpaceFlightsApiMethods spaceFlightsApiMethods() {
        return new SpaceFlightsApiMethods();
    }
}
