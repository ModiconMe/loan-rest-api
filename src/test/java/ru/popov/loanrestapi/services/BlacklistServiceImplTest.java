package ru.popov.loanrestapi.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.popov.loanrestapi.domain.Blacklist;
import ru.popov.loanrestapi.domain.Person;
import ru.popov.loanrestapi.repositories.BlacklistRepository;
import ru.popov.loanrestapi.repositories.PersonRepository;
import ru.popov.loanrestapi.util.exceptions.PersonIsInBlacklistException;
import ru.popov.loanrestapi.util.exceptions.PersonNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BlacklistServiceImplTest {

    private BlacklistService blacklistService;
    @Mock
    private BlacklistRepository blacklistRepository;
    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        blacklistService = new BlacklistServiceImpl(blacklistRepository, personRepository);
    }

    @Test
    void itShouldAddPersonToBlackList() {
        // given
        String name = "Dmitry";
        Person person = new Person(name, "Popov");
        Blacklist blacklist = new Blacklist(person);
        given(personRepository.findByName(name)).willReturn(Optional.of(person));

        // when
        blacklistService.addPersonToBlackList(person);

        // then
        ArgumentCaptor<Blacklist> captor = ArgumentCaptor.forClass(Blacklist.class);
        verify(blacklistRepository).save(captor.capture());
        Blacklist capturedBlacklist = captor.getValue();
        assertThat(blacklist).isEqualTo(capturedBlacklist);
    }

    @Test
    void itShouldNotAddPersonToBlackListAndThrowPersonNotFoundException() {
        // given
        String name = "Dmitry";
        Person person = new Person(name, "Popov");
        Blacklist blacklist = new Blacklist(person);
        given(personRepository.findByName(name)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> blacklistService.addPersonToBlackList(person))
                .isInstanceOf(PersonNotFoundException.class)
                .hasMessageContaining("person with name: " + person.getName() + " does not exist");

        verify(blacklistRepository, never()).save(blacklist);
    }

    @Test
    void itShouldNotAddPersonToBlackListAndThrowPersonIsIbBlacklistException() {
        // given
        String name = "Dmitry";
        Person person = new Person(name, "Popov");
        Blacklist blacklist = new Blacklist(person);
        given(personRepository.findByName(name)).willReturn(Optional.of(person));
        given(blacklistRepository.findByPersonId(person.getId())).willReturn(Optional.of(blacklist));

        // when
        // then
        assertThatThrownBy(() -> blacklistService.addPersonToBlackList(person))
                .isInstanceOf(PersonIsInBlacklistException.class)
                .hasMessageContaining("person with name: " + person.getName() + " is already in blacklist");

        verify(blacklistRepository, never()).save(blacklist);
    }

    @Test
    void itShouldRemovePersonFromBlackList() {
        // given
        String name = "Dmitry";
        Person person = new Person(name, "Popov");
        given(personRepository.findByName(name)).willReturn(Optional.of(person));

        // when
        blacklistService.removePersonFromBlackList(person);

        // then
        verify(blacklistRepository).deleteByPersonId(person.getId());
    }

    @Test
    void itShouldNotRemovePersonFromBlackListAndThrowPersonNotFoundException() {
        // given
        String name = "Dmitry";
        Person person = new Person(name, "Popov");
        given(personRepository.findByName(name)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> blacklistService.removePersonFromBlackList(person))
                .isInstanceOf(PersonNotFoundException.class)
                .hasMessageContaining("person with name: " + person.getName() + " does not exist");

        verify(blacklistRepository, never()).deleteByPersonId(person.getId());
    }
}