package br.com.leomanzini.space.flight.news.repository;

import br.com.leomanzini.space.flight.news.model.ArticleControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleControlCrudRepository extends JpaRepository<ArticleControl, Long> {

    @Query("SELECT COUNT(a) FROM Article a WHERE a.insertedByHuman=false")
    Long apiArticlesCount();
}
