package br.com.leomanzini.space.flights.batch.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Launches {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String provider;
}
