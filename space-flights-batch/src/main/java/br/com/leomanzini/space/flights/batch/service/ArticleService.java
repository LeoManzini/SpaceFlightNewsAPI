package br.com.leomanzini.space.flights.batch.service;

import br.com.leomanzini.space.flights.batch.model.Article;
import br.com.leomanzini.space.flights.batch.repository.ArticleRepository;
import br.com.leomanzini.space.flights.batch.repository.EventsRepository;
import br.com.leomanzini.space.flights.batch.repository.LaunchesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private LaunchesRepository launchesRepository;

    @Value("${space.flights.api.context}")
    private String applicationContext;

    @Value("${space.flights.api.articles.count}")
    private String countArticles;

    @Value("${space.flights.api.all.articles}")
    private String allArticles;

    public void insertNewArticle(Article article) {
        System.out.println(applicationContext);
        System.out.println(countArticles);
        System.out.println(allArticles);
        eventsRepository.saveAll(article.getEvents());
        launchesRepository.saveAll(article.getLaunches());
        articleRepository.save(article);
    }
}
