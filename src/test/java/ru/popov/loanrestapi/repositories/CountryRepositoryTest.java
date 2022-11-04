package ru.popov.loanrestapi.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.popov.loanrestapi.domain.Country;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void itShouldFindCountryByName() {
        // given
        String name = "USA";
        Country country = new Country(name);
        countryRepository.save(country);

        // when
        Optional<Country> expected = countryRepository.findByName(name);

        // then
        assertThat(expected.get()).isEqualTo(country);
    }
}