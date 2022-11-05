package ru.popov.loanrestapi.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.popov.loanrestapi.domain.Country;
import ru.popov.loanrestapi.domain.Loan;
import ru.popov.loanrestapi.domain.Person;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;

    @Test
    void itShouldFindLoanByApprovedIsTrue() {
        // given
        Person person = new Person("Dmitry", "Popov");
        Country country =  new Country("USA");
        Loan loan = new Loan(2500.5, LocalDate.of(2022, 11, 4), true, person, country);

        // when
        loanRepository.save(loan);

        // then
        List<Loan> expected = loanRepository.findByApprovedIsTrue();
        assertThat(expected.contains(loan));
    }

    @Test
    void itShouldFindLoanByPersonIdAndApprovedIsTrue() {
        // given
        Person person = new Person("Dmitry", "Popov");
        Country country =  new Country("USA");
        Loan loan = new Loan(2500.5, LocalDate.of(2022, 11, 4), true, person, country);

        // when
        loanRepository.save(loan);

        // then
        List<Loan> expected = loanRepository.findByPersonIdAndApprovedIsTrue(person.getId());
        assertThat(expected.contains(loan));
    }
}