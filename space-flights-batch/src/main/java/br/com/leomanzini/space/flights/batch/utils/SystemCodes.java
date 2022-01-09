package br.com.leomanzini.space.flights.batch.utils;

public enum SystemCodes {

    SUCCESS(200L),
    ARTICLES_CONTROL_ID(1L);

    private Long code;

    private SystemCodes(Long code) {
        this.code = code;
    }

    public Long getCode() {
        return code;
    }
}
