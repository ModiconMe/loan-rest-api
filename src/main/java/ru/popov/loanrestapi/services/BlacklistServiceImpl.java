package ru.popov.loanrestapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.popov.loanrestapi.domain.Blacklist;
import ru.popov.loanrestapi.domain.Person;
import ru.popov.loanrestapi.repositories.BlacklistRepository;
import ru.popov.loanrestapi.repositories.PersonRepository;
import ru.popov.loanrestapi.util.exceptions.PersonIsInBlacklistException;
import ru.popov.loanrestapi.util.exceptions.PersonNotFoundException;

import java.util.Optional;

@Service
public class BlacklistServiceImpl implements BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final PersonRepository personRepository;

    @Autowired
    public BlacklistServiceImpl(BlacklistRepository blacklistRepository, PersonRepository personRepository) {
        this.blacklistRepository = blacklistRepository;
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public void addPersonToBlackList(Person person) throws PersonNotFoundException, PersonIsInBlacklistException {
        Blacklist blacklist = new Blacklist();

        Optional<Person> optionalPerson = personRepository.findByName(person.getName());
        if (optionalPerson.isEmpty())
            throw new PersonNotFoundException("person with name: " + person.getName() + " does not exist");
        Person addedPerson = optionalPerson.get();

        Optional<Blacklist> optionalBlacklist = blacklistRepository.findByPersonId(addedPerson.getId());
        if (optionalBlacklist.isPresent())
            throw new PersonIsInBlacklistException("person with name: " + addedPerson.getName() + " is already in blacklist");

        blacklist.setPerson(addedPerson);
        blacklistRepository.save(blacklist);
    }

    @Override
    @Transactional
    public void removePersonFromBlackList(Person person) throws PersonNotFoundException {
        Optional<Person> optionalPerson = personRepository.findByName(person.getName());

        if (optionalPerson.isEmpty())
            throw new PersonNotFoundException("person with name: " + person.getName() + " does not exist");

        Person removedPerson = optionalPerson.get();
        blacklistRepository.deleteByPersonId(removedPerson.getId());
    }
}
