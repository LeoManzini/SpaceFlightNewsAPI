package br.com.leomanzini.space.flights.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailRequestDTO {

    private String name;
    private String to;
    private String from;
    private String subject;
}
