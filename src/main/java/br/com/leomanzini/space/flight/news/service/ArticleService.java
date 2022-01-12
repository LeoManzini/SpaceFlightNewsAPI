package br.com.leomanzini.space.flight.news.service;

import br.com.leomanzini.space.flight.news.dto.ArticlesDTO;
import br.com.leomanzini.space.flight.news.model.Article;
import br.com.leomanzini.space.flight.news.repository.ArticleRepository;
import br.com.leomanzini.space.flight.news.utils.beans.ModelMapperMethods;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;

@Service
@AllArgsConstructor
public class ArticleService {

    @Autowired
    private final ArticleRepository articleRepository;
    @Autowired
    private final ModelMapperMethods modelMapper;

    @Transactional(readOnly = true)
    public Page<ArticlesDTO> findAll(Pageable pageable) {
        Page<Article> articlePage = articleRepository.findAll(pageable);
        return articlePage.map(article -> modelMapper.entityToDto(article));
    }

    @Transactional(readOnly = true)
    public ArticlesDTO findById(Long id) {
        return modelMapper.entityToDto(articleRepository.findById(id).orElseThrow());
    }
}
