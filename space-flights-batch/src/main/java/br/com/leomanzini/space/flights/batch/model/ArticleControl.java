package br.com.leomanzini.space.flights.batch.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "article_count", nullable = false)
    private Long articleCount;

    @Column(name = "last_article_id", nullable = false)
    private Long lastArticleId;
}
