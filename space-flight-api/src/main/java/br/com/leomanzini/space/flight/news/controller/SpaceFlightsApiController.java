package br.com.leomanzini.space.flight.news.controller;

import br.com.leomanzini.space.flight.news.dto.ArticlesDTO;
import br.com.leomanzini.space.flight.news.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class SpaceFlightsApiController {

    @Autowired
    private final ArticleService articleService;

    public SpaceFlightsApiController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<Page<ArticlesDTO>> findAll(Pageable pageable) {
        Page<ArticlesDTO> articlesDTOList = articleService.findAll(pageable);
        return ResponseEntity.ok(articlesDTOList);
    }
}
