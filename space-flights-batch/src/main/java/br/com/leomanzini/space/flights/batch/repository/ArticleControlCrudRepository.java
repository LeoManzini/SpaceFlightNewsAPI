package br.com.leomanzini.space.flights.batch.repository;

import br.com.leomanzini.space.flights.batch.model.ArticleControl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleControlCrudRepository extends CrudRepository<ArticleControl, Long> {

    @Query("SELECT COUNT(a) FROM Article a WHERE a.insertedByHuman=false")
    Long apiArticlesCount();
}
