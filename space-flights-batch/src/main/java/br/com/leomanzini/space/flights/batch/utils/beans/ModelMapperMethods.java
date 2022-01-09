package br.com.leomanzini.space.flights.batch.utils.beans;

import br.com.leomanzini.space.flights.batch.dto.ArticlesResponseDTO;
import br.com.leomanzini.space.flights.batch.model.Article;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperMethods {

    @Autowired
    private ModelMapper modelMapper;

    public Article dtoToEntity(ArticlesResponseDTO articleDto) {
        return modelMapper.map(articleDto, Article.class);
    }

    public ArticlesResponseDTO entityToDto(Article article) {
        return modelMapper.map(article, ArticlesResponseDTO.class);
    }
}
