package br.com.leomanzini.space.flight.news.utils.enums;

public enum SystemMessages {

    ARTICLE_NOT_FOUND("Article not found at database with ID "),
    ARTICLE_ALREADY_IN_DATABASE("Article already found at database with ID "),
    ARTICLE_INSERTED_SUCCESS("New article inserted successfully with ID ");

    private String message;

    private SystemMessages(String message) {
        this.message = message;
    }

    public String getMessage () {
        return message;
    }
}
