package ru.popov.loanrestapi.services;

import org.springframework.transaction.annotation.Transactional;
import ru.popov.loanrestapi.domain.Person;
import ru.popov.loanrestapi.util.exceptions.PersonNotFoundException;

/**
 * Реализован функционал добавления и удаления человека их черного списка
 */
public interface BlacklistService {
    @Transactional
    void addPersonToBlackList(Person person) throws PersonNotFoundException;

    @Transactional
    void removePersonFromBlackList(Person person) throws PersonNotFoundException;
}
