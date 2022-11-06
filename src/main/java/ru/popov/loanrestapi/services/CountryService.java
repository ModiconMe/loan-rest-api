package ru.popov.loanrestapi.services;

import org.springframework.transaction.annotation.Transactional;
import ru.popov.loanrestapi.domain.Country;

/**
 * Реализован функционал добавления новой страны
 */
public interface CountryService {
    @Transactional
    void addCountry(Country country);
}
