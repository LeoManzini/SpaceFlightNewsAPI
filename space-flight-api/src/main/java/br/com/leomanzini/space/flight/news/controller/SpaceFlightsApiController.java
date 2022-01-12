package br.com.leomanzini.space.flight.news.controller;

import br.com.leomanzini.space.flight.news.dto.ArticlesDTO;
import br.com.leomanzini.space.flight.news.dto.ResponseEntityDTO;
import br.com.leomanzini.space.flight.news.exceptions.ArticleAlreadyAtDatabaseException;
import br.com.leomanzini.space.flight.news.exceptions.ArticleNotFoundException;
import br.com.leomanzini.space.flight.news.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/articles")
public class SpaceFlightsApiController {

    @Autowired
    private final ArticleService articleService;

    public SpaceFlightsApiController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<Page<ArticlesDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(articleService.findAll(pageable));
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<ArticlesDTO> findById(@PathVariable("id") Long id) throws ArticleNotFoundException {
        return ResponseEntity.ok(articleService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseEntityDTO> save(@RequestBody @Valid ArticlesDTO articleDTO) throws ArticleAlreadyAtDatabaseException {
        return ResponseEntity.ok(articleService.createArticle(articleDTO));
    }
}
