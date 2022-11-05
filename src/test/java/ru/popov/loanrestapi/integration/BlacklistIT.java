package ru.popov.loanrestapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.popov.loanrestapi.domain.Blacklist;
import ru.popov.loanrestapi.domain.Person;
import ru.popov.loanrestapi.repositories.BlacklistRepository;
import ru.popov.loanrestapi.repositories.PersonRepository;
import ru.popov.loanrestapi.services.BlacklistService;
import ru.popov.loanrestapi.web.BlacklistController;

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
public class BlacklistIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlacklistService blacklistService;

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
        assertThat(optionalBlacklist.get().getPerson().getId()).isEqualTo(person.getId());
        blacklistRepository.deleteById(optionalBlacklist.get().getId());
        personRepository.deleteById(person.getId());
    }
}
