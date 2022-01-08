package br.com.leomanzini.space.flights.batch.dto;

import br.com.leomanzini.space.flights.batch.model.Launches;

import java.util.List;

public class SpaceFlightsResponseDTO {

    private Long id;
    private String title;
    private String url;
    private String imageUrl;
    private String newsSite;
    private String summary;
    private String publishedAt;
    private String updatedAt;
    private Boolean featured;
    private List<LaunchesDTO> launches;
    private List<EventsDTO> events;
}
