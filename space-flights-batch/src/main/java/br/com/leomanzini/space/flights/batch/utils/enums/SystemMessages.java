package br.com.leomanzini.space.flights.batch.utils.enums;

public enum SystemMessages {

    HTTP_ERROR("HTTP error code: "),
    DATABASE_NOT_FOUND("Entity not found at database"),
    NOT_FOUND_ERROR("Entity API not found"),
    UPDATE_ROUTINE_ERROR("Could not complete the database update routine"),
    PERSIST_ARTICLE_LIST_ERROR("Error while persisting article list"),
    ARTICLE_FOUND_AT_DATABASE("Article already at database"),
    WRITE_ERROR("Error while writing historical insertion"),
    HISTORICAL_ROUTINE_API_ERROR("Error while accessing API"),
    HISTORICAL_ROUTINE_WRITE_ERROR("Error while writing file");

    private String message;

    SystemMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
