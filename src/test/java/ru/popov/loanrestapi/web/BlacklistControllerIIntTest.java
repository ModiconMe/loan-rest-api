package ru.popov.loanrestapi.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import ru.popov.loanrestapi.domain.Blacklist;
import ru.popov.loanrestapi.domain.Person;
import ru.popov.loanrestapi.repositories.BlacklistRepository;
import ru.popov.loanrestapi.repositories.PersonRepository;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-it.properties")
@AutoConfigureMockMvc
@Transactional
public class BlacklistControllerIIntTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BlacklistRepository blacklistRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void canAddPersonToBlacklist() throws Exception {
        // given
        Person person = new Person("IT_name", "IT_surname", Collections.emptyList());
        personRepository.save(person);

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/blacklist/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)));

        // then
        resultActions.andExpect(status().isOk());
        Optional<Blacklist> optionalBlacklist = blacklistRepository.findByPersonId(person.getId());
        assertThat(optionalBlacklist.isPresent()).isTrue();

        Blacklist blacklist = optionalBlacklist.get();
        assertThat(blacklist.getPerson()).isEqualTo(person);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/person.csv", numLinesToSkip = 1)
    void canNotAddPersonToBlacklist(String name, String surname) throws Exception {
        // given
        Person person = new Person(name, surname, Collections.emptyList());

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/blacklist/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void canRemovePersonToBlacklist() throws Exception {
        // given
        Person person = new Person("IT_name", "IT_surname", Collections.emptyList());
        personRepository.save(person);

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/blacklist/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)));

        // then
        resultActions.andExpect(status().isOk());
        Optional<Blacklist> optionalBlacklist = blacklistRepository.findByPersonId(person.getId());
        assertThat(optionalBlacklist.isPresent()).isFalse();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/person.csv", numLinesToSkip = 1)
    void canNotRemovePersonToBlacklist(String name, String surname) throws Exception {
        // given
        Person person = new Person(name, surname, Collections.emptyList());

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/blacklist/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }
}
