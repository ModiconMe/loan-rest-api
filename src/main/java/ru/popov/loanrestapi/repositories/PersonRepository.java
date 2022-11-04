package ru.popov.loanrestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.popov.loanrestapi.domain.Person;

import java.util.Optional;

@Repository
public interface PersonRepository  extends JpaRepository<Person, Integer> {

    Optional<Person> findByName(String name);
}
