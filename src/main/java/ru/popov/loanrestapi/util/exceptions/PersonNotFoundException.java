package ru.popov.loanrestapi.util.exceptions;

public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(String msg) {
        super(msg);
    }
}
