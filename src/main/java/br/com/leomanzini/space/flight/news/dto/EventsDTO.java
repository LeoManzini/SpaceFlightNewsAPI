package br.com.leomanzini.space.flight.news.dto;

import br.com.leomanzini.space.flight.news.model.Events;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventsDTO implements Serializable {

    @NotEmpty
    @EqualsAndHashCode.Include
    private Long id;

    @NotEmpty
    private String provider;
}
