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
import ru.popov.loanrestapi.domain.Person;
import ru.popov.loanrestapi.repositories.PersonRepository;

import java.util.Collections;
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
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void canAddPerson() throws Exception {
        // given
        Person person = new Person("IT_name", "IT_surname");

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/people/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)));

        // then
        resultActions.andExpect(status().isOk());
        Optional<Person> optionalPerson = personRepository.findByName(person.getName());
        assertThat(optionalPerson.isPresent()).isTrue();

        assertThat(optionalPerson.get()).isEqualTo(person);
    }
}