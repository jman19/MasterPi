package com.masterPi.ExceptionHandler;

import org.springframework.http.HttpStatus;

public class CustomRunTimeException extends RuntimeException {
    private HttpStatus error;

    public CustomRunTimeException(String message, HttpStatus error) {
        super(message);
        this.error = error;
    }

    public HttpStatus getError() {
        return error;
    }

    public void setError(HttpStatus error) {
        this.error = error;
    }
}
