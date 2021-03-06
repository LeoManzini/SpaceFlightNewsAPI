package br.com.leomanzini.space.flights.batch.utils.enums;

public enum SystemMessages {

    EMAIL_HISTORICAL_REPORT("Historical report executed successfully"),
    DATABASE_UPDATED_REPORT("Database update based on API executed successfully"),
    DATABASE_ALREADY_UPDATED_REPORT("Database already updated with API"),
    MAIL_API_ERROR("Your execution has errors while accessing Space FLights API, control table was updated to fix error"),
    MAIL_WRITE_ERROR("Application found errors while writing the historical insert registers, control table was updated to fix error"),
    MAIL_PERSISTENCE_ERROR("Application found errors while persisting new articles to database, control table was updated to fix error"),
    MAIL_HISTORICAL_GENERIC_ERROR("Application finished with errors, executing historical routine"),
    MAIL_UPDATE_GENERIC_ERROR("Application finished with errors, executing database update routine"),
    HTTP_ERROR("HTTP error code: "),
    DATABASE_NOT_FOUND("Entity not found at database"),
    NOT_FOUND_ERROR("Entity API not found"),
    UPDATE_ROUTINE_ERROR("Could not complete the database update routine"),
    API_GET_ARTICLE_ID("API error get article by id"),
    INSERT_LISTS_ERROR("Error persisting articles to database"),
    PERSIST_ARTICLE_LIST_ERROR("Error while persisting article list"),
    ARTICLE_FOUND_AT_DATABASE("Article already at database"),
    WRITE_ERROR("Error while writing historical insertion"),
    EMAIL_SEND_ERROR("Error while trying to mount email"),
    HISTORICAL_ROUTINE_API_ERROR("Error while accessing API"),
    HISTORICAL_ROUTINE_WRITE_ERROR("Error while writing file"),
    HISTORICAL_ROUTINE_EMAIL_ERROR("Error sending email");

    private String message;

    SystemMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
