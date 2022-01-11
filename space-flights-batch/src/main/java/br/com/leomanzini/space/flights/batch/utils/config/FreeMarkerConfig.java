package br.com.leomanzini.space.flights.batch.utils.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class FreeMarkerConfig {

    @Value("${mail.template.path}")
    private String templatePath;

    @Bean
    @Primary
    public FreeMarkerConfigurationFactoryBean factoryBean () {
        FreeMarkerConfigurationFactoryBean factoryBean = new FreeMarkerConfigurationFactoryBean();
        factoryBean.setTemplateLoaderPath(templatePath);
        return factoryBean;
    }
}
