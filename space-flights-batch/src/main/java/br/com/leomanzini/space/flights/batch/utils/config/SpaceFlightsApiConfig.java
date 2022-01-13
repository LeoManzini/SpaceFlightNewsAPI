package br.com.leomanzini.space.flights.batch.utils.config;

import br.com.leomanzini.space.flights.batch.utils.beans.SpaceFlightsApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpaceFlightsApiConfig {

    @Bean
    public SpaceFlightsApi spaceFlightsApiMethods() {
        return new SpaceFlightsApi();
    }
}
