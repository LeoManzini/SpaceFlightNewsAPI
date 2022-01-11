package br.com.leomanzini.space.flights.batch.utils.beans;

import br.com.leomanzini.space.flights.batch.dto.ArticlesDTO;
import br.com.leomanzini.space.flights.batch.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperMethods {

    @Autowired
    private org.modelmapper.ModelMapper modelMapper;

    public Article dtoToEntity(ArticlesDTO articleDto) {
        return modelMapper.map(articleDto, Article.class);
    }

    public ArticlesDTO entityToDto(Article article) {
        return modelMapper.map(article, ArticlesDTO.class);
    }
}
