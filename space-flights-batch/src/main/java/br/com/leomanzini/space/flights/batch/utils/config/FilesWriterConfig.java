package br.com.leomanzini.space.flights.batch.utils.config;

import br.com.leomanzini.space.flights.batch.utils.beans.FilesWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilesWriterConfig {

    @Bean
    public FilesWriter fileWriter() {
        return new FilesWriter();
    }
}
