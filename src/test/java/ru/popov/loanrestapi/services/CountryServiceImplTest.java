package ru.popov.loanrestapi.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.popov.loanrestapi.domain.Blacklist;
import ru.popov.loanrestapi.domain.Country;
import ru.popov.loanrestapi.repositories.CountryRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CountryServiceImplTest {

    private CountryService countryService;
    @Mock
    private CountryRepository countryRepository;

    @BeforeEach
    void setUp() {
        countryService = new CountryServiceImpl(countryRepository);
    }

    @Test
    void itShouldAddCountry() {
        // given
        Country country = new Country("USA");

        // when
        countryService.addCountry(country);

        // then
        ArgumentCaptor<Country> captor = ArgumentCaptor.forClass(Country.class);
        verify(countryRepository).save(captor.capture());
        Country capturedCountry = captor.getValue();
        assertThat(country).isEqualTo(capturedCountry);
    }
}