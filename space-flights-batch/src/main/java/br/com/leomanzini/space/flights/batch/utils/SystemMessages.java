package br.com.leomanzini.space.flights.batch.utils;

public enum SystemMessages {

    HTTP_ERROR("HTTP error code: "),
    DATABASE_NOT_FOUND("Entity not found at database"),
    NOT_FOUND_ERROR("Entity API not found"),
    UPDATE_ROUTINE_ERROR("Could not complete the database update routine"),
    PERSIST_ARTICLE_LIST_ERROR("Error while persisting article list"),
    ARTICLE_FOUND_AT_DATABASE("Article already at database");

    private String message;

    private SystemMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
