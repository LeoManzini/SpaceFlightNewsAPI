package br.com.leomanzini.space.flights.batch.utils.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Configuration
public class BufferedWriterConfig {

    @Value("${file.path}")
    private String filePath;

    @Bean
    public BufferedWriter bufferedWriter() throws IOException {
        return new BufferedWriter(new FileWriter(filePath, true));
    }
}
