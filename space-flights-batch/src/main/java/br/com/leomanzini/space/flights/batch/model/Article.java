package br.com.leomanzini.space.flights.batch.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private Boolean featured;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "news_site", nullable = false)
    private String newsSite;

    @Column(nullable = false)
    private String summary;

    @Column(name = "published_at", nullable = false)
    private String publishedAt;

    @OneToMany
    private List<Launches> launches;

    @OneToMany
    private List<Events> events;
}
