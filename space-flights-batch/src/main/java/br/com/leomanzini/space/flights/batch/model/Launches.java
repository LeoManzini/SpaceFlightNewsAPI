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
public class Launches {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String provider;
}
