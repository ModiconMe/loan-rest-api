package ru.popov.loanrestapi.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.popov.loanrestapi.domain.Blacklist;
import ru.popov.loanrestapi.domain.Person;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class BlacklistRepositoryTest {

    @Autowired
    private BlacklistRepository blacklistRepository;

    @Test
    void itShouldFindPersonInBlacklistByPersonId() {
        // given
        Person person = new Person("Dmitry", "Popov");
        Blacklist blacklist = new Blacklist(person);
        blacklistRepository.save(blacklist);

        // when
        Optional<Blacklist> expected = blacklistRepository.findByPersonId(person.getId());

        // then
        assertThat(expected.get()).isEqualTo(blacklist);
    }

    @Test
    void itShouldDeletePersonInBlacklistByPersonId() {
        // given
        Person person = new Person("Dmitry", "Popov");
        Blacklist blacklist = new Blacklist(person);

        // when
        blacklistRepository.deleteByPersonId(person.getId());
        Optional<Blacklist> expected = blacklistRepository.findById(blacklist.getId());

        // then
        assertThat(expected.isEmpty()).isTrue();
    }
}