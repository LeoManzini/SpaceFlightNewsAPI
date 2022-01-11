package br.com.leomanzini.space.flight.news.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private Boolean featured;

    @Column(nullable = false, length=10485760)
    private String title;

    @Column(nullable = false, length=10485760)
    private String url;

    @Column(name = "image_url", nullable = false, length=10485760)
    private String imageUrl;

    @Column(name = "news_site", nullable = false, length=10485760)
    private String newsSite;

    @Column(nullable = false, length=10485760)
    private String summary;

    @Column(name = "published_at", nullable = false)
    private String publishedAt;

    @ManyToMany
    private List<Launches> launches;

    @ManyToMany
    private List<Events> events;

    @Column(name = "inserted_by_human")
    private Boolean insertedByHuman = false;
}