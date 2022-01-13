package br.com.leomanzini.space.flight.news.controller;

import br.com.leomanzini.space.flight.news.utils.enums.SystemMessages;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ApiStatusController {

    @GetMapping
    public String getStatus() {
        return SystemMessages.API_STATUS_MESSAGE.getMessage();
    }
}
