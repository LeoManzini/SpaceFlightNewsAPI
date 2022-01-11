package br.com.leomanzini.space.flight.news.repository;

import br.com.leomanzini.space.flight.news.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends JpaRepository<Events, Long> {
}
