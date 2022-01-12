package br.com.leomanzini.space.flight.news.dto;

import br.com.leomanzini.space.flight.news.model.Launches;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaunchesDTO implements Serializable {

    @NotEmpty
    @EqualsAndHashCode.Include
    private String id;

    @NotEmpty
    private String provider;

    public LaunchesDTO(Launches launch) {
        id = launch.getId();
        provider = launch.getProvider();
    }
}
