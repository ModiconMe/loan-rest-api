package ru.popov.loanrestapi.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.popov.loanrestapi.domain.Person;
import ru.popov.loanrestapi.repositories.PersonRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    private PersonService personService;
    @Mock private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personService = new PersonServiceImpl(personRepository);
    }

    @Test
    @DisplayName("It should add person")
    void itShouldAddPerson() {
        // given
        Person person = new Person("Dmitry", "Popov");

        // when
        personService.addPerson(person);

        // then
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(captor.capture());
        Person capturedPerson = captor.getValue();
        assertThat(person).isEqualTo(capturedPerson);
    }
}