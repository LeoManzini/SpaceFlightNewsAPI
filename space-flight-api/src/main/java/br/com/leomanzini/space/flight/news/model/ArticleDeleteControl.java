package br.com.leomanzini.space.flight.news.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDeleteControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article_excluded_id")
    private Long articleExcluded;

    @Column(name = "exclusion_date")
    private LocalDateTime exclusionDay;
}
