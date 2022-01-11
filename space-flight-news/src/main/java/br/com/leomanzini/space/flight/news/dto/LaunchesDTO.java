package br.com.leomanzini.space.flight.news.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaunchesDTO {

    @NotEmpty
    @EqualsAndHashCode.Include
    private String id;

    @NotEmpty
    private String provider;
}
