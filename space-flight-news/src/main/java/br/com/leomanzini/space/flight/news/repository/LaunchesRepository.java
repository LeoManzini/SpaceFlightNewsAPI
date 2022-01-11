package br.com.leomanzini.space.flight.news.repository;

import br.com.leomanzini.space.flight.news.model.Launches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaunchesRepository extends JpaRepository<Launches, String> {
}
