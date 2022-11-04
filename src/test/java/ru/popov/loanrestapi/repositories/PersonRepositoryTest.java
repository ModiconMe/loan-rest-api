package ru.popov.loanrestapi.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.popov.loanrestapi.domain.Person;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void itShouldFindPersonByName() {
        // given
        String name = "Dmitry";
        Person person = new Person(name, "Popov");
        personRepository.save(person);

        // when
        Optional<Person> expected = personRepository.findByName(name);

        // then
        assertThat(expected.get()).isEqualTo(person);
    }
}