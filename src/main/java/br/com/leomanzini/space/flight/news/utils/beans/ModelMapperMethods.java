package br.com.leomanzini.space.flight.news.utils.beans;

import br.com.leomanzini.space.flight.news.dto.ArticlesDTO;
import br.com.leomanzini.space.flight.news.model.Article;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperMethods {

    @Autowired
    private final ModelMapper modelMapper;

    public ModelMapperMethods(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
    }

    public Article dtoToEntity(ArticlesDTO articleDto) {
        return modelMapper.map(articleDto, Article.class);
    }

    public ArticlesDTO entityToDto(Article article) {
        return modelMapper.map(article, ArticlesDTO.class);
    }
}
