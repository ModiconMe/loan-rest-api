package ru.popov.loanrestapi.services;

import org.springframework.transaction.annotation.Transactional;
import ru.popov.loanrestapi.domain.Person;

/**
 * Добавлен функционал добавления и удаления человека из БД
 */
public interface PersonService {
    @Transactional
    void addPerson(Person person);
}
