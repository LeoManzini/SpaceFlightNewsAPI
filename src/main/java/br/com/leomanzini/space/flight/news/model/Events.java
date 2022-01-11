package br.com.leomanzini.space.flight.news.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

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
