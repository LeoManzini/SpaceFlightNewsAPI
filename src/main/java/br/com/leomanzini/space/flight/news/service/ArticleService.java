package br.com.leomanzini.space.flight.news.service;

import br.com.leomanzini.space.flight.news.dto.ArticlesDTO;
import br.com.leomanzini.space.flight.news.model.Article;
import br.com.leomanzini.space.flight.news.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArticleService {

    @Autowired
    private final ArticleRepository articleRepository;

    public Page<ArticlesDTO> findAll(Pageable pageable) {
        Page<Article> articleList = articleRepository.findAll(pageable);
        return articleList.map(article -> new ArticlesDTO(article));
    }
}
