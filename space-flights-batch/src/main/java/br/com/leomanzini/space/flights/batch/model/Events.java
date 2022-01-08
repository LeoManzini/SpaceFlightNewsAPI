package br.com.leomanzini.space.flights.batch.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Events {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    private String provider;
}
