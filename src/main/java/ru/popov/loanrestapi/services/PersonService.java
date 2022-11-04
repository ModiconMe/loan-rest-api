package ru.popov.loanrestapi.services;

import org.springframework.transaction.annotation.Transactional;
import ru.popov.loanrestapi.domain.Person;

public interface PersonService {
    @Transactional
    void addPerson(Person person);
}
