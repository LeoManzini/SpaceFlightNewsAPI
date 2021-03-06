package br.com.leomanzini.space.flight.news.exceptions;

import br.com.leomanzini.space.flight.news.utils.enums.SystemMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArticleNotFoundException extends Exception {

    private static final long serialVersionUID = -6374494466178545567L;

    public ArticleNotFoundException(Long id) {
        super(SystemMessages.ARTICLE_NOT_FOUND.getMessage() + id);
    }
}
