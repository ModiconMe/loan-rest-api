package ru.popov.loanrestapi.util;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiException {
    private final String message;
    private final Class<?> aClass;
    private HttpStatus httpStatus;
    private ZonedDateTime timestamp;

    public ApiException(String message, Class<?> aClass, HttpStatus httpStatus, ZonedDateTime timestamp) {
        this.message = message;
        this.aClass = aClass;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Class getaClass() {
        return aClass;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
