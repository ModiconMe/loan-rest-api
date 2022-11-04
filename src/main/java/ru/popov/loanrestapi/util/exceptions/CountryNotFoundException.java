package ru.popov.loanrestapi.util.exceptions;

public class CountryNotFoundException extends RuntimeException {

    public CountryNotFoundException(String msg) {
        super(msg);
    }
}
