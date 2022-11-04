package ru.popov.loanrestapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.popov.loanrestapi.domain.Person;
import ru.popov.loanrestapi.repositories.PersonRepository;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public void addPerson(Person person) {
        personRepository.save(person);
    }
}
