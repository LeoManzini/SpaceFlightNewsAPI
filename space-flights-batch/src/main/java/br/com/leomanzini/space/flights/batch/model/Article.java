package br.com.leomanzini.space.flights.batch.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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
    @JoinColumn(name = "launches_id")
    private List<Launches> launches;

    @OneToMany
    @JoinColumn(name = "events_id")
    private  List<Events> events;
}
