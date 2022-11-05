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
import ru.popov.loanrestapi.domain.Loan;
import ru.popov.loanrestapi.domain.Person;
import ru.popov.loanrestapi.repositories.CountryRepository;
import ru.popov.loanrestapi.repositories.LoanRepository;
import ru.popov.loanrestapi.repositories.PersonRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
@Transactional
class LoanControllerIntTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void canAddLoan() throws Exception {
        // given
        Person person = new Person("IT_name", "IT_surname", Collections.emptyList());
        Country country = new Country("IT_name");
        personRepository.save(person);
        countryRepository.save(country);

        Loan loan = new Loan(2000.0, LocalDate.of(2022, 11, 5), false, person, country);

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/loans/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)));

        // then
        resultActions.andExpect(status().isOk());
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans.contains(loan)).isTrue();
    }

    @Test
    void canNotAddLoanBecausePersonIsInvalid() throws Exception {
        // given
        Person person = new Person("IT_name", "IT_surname", Collections.emptyList());
        Country country = new Country("IT_name");
        countryRepository.save(country);

        Loan loan = new Loan(2000.0, LocalDate.of(2022, 11, 5), false, person, country);

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/loans/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void canNotAddLoanBecauseCountryIsInvalid() throws Exception {
        // given
        Person person = new Person("IT_name", "IT_surname", Collections.emptyList());
        Country country = new Country("IT_name");
        personRepository.save(person);

        Loan loan = new Loan(2000.0, LocalDate.of(2022, 11, 5), false, person, country);

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/loans/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void canApproveLoan() throws Exception {
        // given
        Person person = new Person("IT_name", "IT_surname", Collections.emptyList());
        Country country = new Country("IT_name");
        personRepository.save(person);
        countryRepository.save(country);

        Loan loan = new Loan(2000.0, LocalDate.of(2022, 11, 5), false, person, country);
        loanRepository.save(loan);
        int id = loan.getId();
        // when
        ResultActions resultActions = mockMvc
                .perform(patch("/loans/approved/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)));

        // then
        resultActions.andExpect(status().isOk());
        List<Loan> loans = loanRepository.findByApprovedIsTrue();
        assertThat(loans.contains(loan)).isTrue();
    }

    @Test
    void canNotApproveLoanBecauseLoanIsInvalid() throws Exception {
        // given
        Loan loan = new Loan(2000.0, LocalDate.of(2022, 11, 5), false, null, null);
        int id = loan.getId();
        // when
        ResultActions resultActions = mockMvc
                .perform(patch("/loans/approved/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void canGetApprovedLoans() throws Exception {
        // given
        Person person = new Person("IT_name", "IT_surname", Collections.emptyList());
        Country country = new Country("IT_name");
        personRepository.save(person);
        countryRepository.save(country);

        Loan loan = new Loan(2000.0, LocalDate.of(2022, 11, 5), true, person, country);
        loanRepository.save(loan);

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/loans/approved/")
                                .param("person_name", person.getName()));

        // then
        resultActions.andExpect(status().isOk());
    }
}