package ru.popov.loanrestapi.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import ru.popov.loanrestapi.domain.Country;
import ru.popov.loanrestapi.repositories.CountryRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
@Transactional
class CountryControllerIntTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void canAddCountry() throws Exception {
        // given
        Country country = new Country("IT_name");

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/countries/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(country)));

        // then
        resultActions.andExpect(status().isOk());
        Optional<Country> optionalCountry = countryRepository.findByName(country.getName());
        assertThat(optionalCountry.isPresent()).isTrue();
    }
}