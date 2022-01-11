package br.com.leomanzini.space.flights.batch.exceptions;

public class EmailServiceException  extends RuntimeException {

    public EmailServiceException(String message) {
        super(message);
    }
}
