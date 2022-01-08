package br.com.leomanzini.space.flights.batch.utils;

public enum ResponseCodes {

    SUCCESS(200);

    private Integer responseCode;

    private ResponseCodes(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Integer getResponseCode() {
        return responseCode;
    }
}
