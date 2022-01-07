package br.com.leomanzini.space.flights.batch.repository;

import br.com.leomanzini.space.flights.batch.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
