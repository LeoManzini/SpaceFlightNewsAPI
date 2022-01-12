package br.com.leomanzini.space.flight.news.exceptions;

import br.com.leomanzini.space.flight.news.utils.enums.SystemMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class ArticleAlreadyAtDatabaseException extends Exception {

    private static final long serialVersionUID = -6374494466178545568L;

    public ArticleAlreadyAtDatabaseException(Long id) {
        super(SystemMessages.ARTICLE_ALREADY_IN_DATABASE.getMessage() + id);
    }
}
