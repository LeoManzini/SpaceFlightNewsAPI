package br.com.leomanzini.space.flights.batch.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventsDTO {

    @EqualsAndHashCode.Include
    private Long id;
    private String provider;
}
