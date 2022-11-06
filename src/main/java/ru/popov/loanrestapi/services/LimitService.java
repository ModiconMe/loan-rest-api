package ru.popov.loanrestapi.services;

import org.springframework.stereotype.Service;

/**
 * Добавлен функционал ограничения кол-ва запросов клиентов из одной страны
 */
public interface LimitService {
    boolean isLimit(String locale);
}
