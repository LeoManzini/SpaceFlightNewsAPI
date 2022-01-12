package br.com.leomanzini.space.flight.news.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

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
}
