package ru.popov.loanrestapi.util.exceptions;

public class RequestLimitException extends RuntimeException {

    public RequestLimitException(String msg) {
        super(msg);
    }
}
