package ru.popov.loanrestapi.util.exceptions;

public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException(String msg) {
        super(msg);
    }
}
