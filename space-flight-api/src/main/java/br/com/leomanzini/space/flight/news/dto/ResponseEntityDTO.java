package br.com.leomanzini.space.flight.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEntityDTO {

    private String message;
    private LocalDateTime insertionDate = LocalDateTime.now();
}
