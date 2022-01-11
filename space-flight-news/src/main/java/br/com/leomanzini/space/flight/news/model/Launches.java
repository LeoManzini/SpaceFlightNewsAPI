package br.com.leomanzini.space.flight.news.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

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
