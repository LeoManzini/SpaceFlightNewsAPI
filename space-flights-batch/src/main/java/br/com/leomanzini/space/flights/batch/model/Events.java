package br.com.leomanzini.space.flights.batch.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Events {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    private String provider;
}
