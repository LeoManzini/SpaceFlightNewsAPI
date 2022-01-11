package br.com.leomanzini.space.flight.news.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SpaceFlightsApiStatusController {

    @GetMapping
    public String getStatus() {
        return "Back-end Challenge 2021 \uD83C\uDFC5 - Space Flight News";
    }
}
