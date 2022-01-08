package br.com.leomanzini.space.flights.batch.utils;

public enum SystemMessages {

    HTTP_ERROR("HTTP error code: ");

    private String message;

    private SystemMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
