package ru.popov.loanrestapi.services;

import org.springframework.stereotype.Service;

public interface LimitService {
    boolean isLimit(String locale);
}
