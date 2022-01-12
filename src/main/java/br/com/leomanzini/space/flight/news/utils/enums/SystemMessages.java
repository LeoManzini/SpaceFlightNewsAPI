package br.com.leomanzini.space.flight.news.utils.enums;

public enum SystemMessages {

    ARTICLE_NOT_FOUND("Article not found at database, to insert use POST method with ID "),
    ARTICLE_ALREADY_IN_DATABASE("Article already found at database, to update use PUT method with ID "),
    ARTICLE_INSERTED_SUCCESS("New article inserted successfully with ID "),
    ARTICLE_UPDATED_SUCCESS("Article updated successfully with ID "),
    ARTICLE_DELETED_SUCCESS("Article deleted from database");

    private String message;

    private SystemMessages(String message) {
        this.message = message;
    }

    public String getMessage () {
        return message;
    }
}
