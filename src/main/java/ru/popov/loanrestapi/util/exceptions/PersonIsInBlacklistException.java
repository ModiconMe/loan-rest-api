package ru.popov.loanrestapi.util.exceptions;

public class PersonIsInBlacklistException extends RuntimeException {

    public PersonIsInBlacklistException(String msg) {
        super(msg);
    }
}
