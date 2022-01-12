package br.com.leomanzini.space.flight.news.dto;

import br.com.leomanzini.space.flight.news.model.Article;
import lombok.*;

import javax.management.ConstructorParameters;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticlesDTO implements Serializable {

    @NotEmpty
    @EqualsAndHashCode.Include
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String url;

    @NotEmpty
    private String imageUrl;

    @NotEmpty
    private String newsSite;

    @NotEmpty
    private String summary;

    @NotEmpty
    private String publishedAt;

    @NotEmpty
    private String updatedAt;

    @NotEmpty
    private Boolean featured;

    @Valid
    private List<LaunchesDTO> launches;

    @Valid
    private List<EventsDTO> events;

    public ArticlesDTO (Article article) {
        id = article.getId();
        title = article.getTitle();
        url = article.getUrl();
        imageUrl = article.getImageUrl();
        newsSite = article.getNewsSite();
        summary = article.getSummary();
        publishedAt = article.getPublishedAt();
        updatedAt = article.getPublishedAt();
        featured = article.getFeatured();
        launches = article.getLaunches().stream().map(launch -> new LaunchesDTO(launch)).collect(Collectors.toList());
        events = article.getEvents().stream().map(event -> new EventsDTO(event)).collect(Collectors.toList());
    }
}
