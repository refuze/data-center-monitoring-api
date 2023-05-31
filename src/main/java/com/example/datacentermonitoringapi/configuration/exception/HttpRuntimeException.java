package com.example.datacentermonitoringapi.configuration.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpRuntimeException extends RuntimeException {
    private final HttpStatus httpStatus;

    public HttpRuntimeException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpRuntimeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
