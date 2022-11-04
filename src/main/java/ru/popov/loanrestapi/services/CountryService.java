package ru.popov.loanrestapi.services;

import org.springframework.transaction.annotation.Transactional;
import ru.popov.loanrestapi.domain.Country;

public interface CountryService {
    @Transactional
    void addCountry(Country country);
}
