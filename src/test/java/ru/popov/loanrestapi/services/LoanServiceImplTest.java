package ru.popov.loanrestapi.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.popov.loanrestapi.domain.Blacklist;
import ru.popov.loanrestapi.domain.Country;
import ru.popov.loanrestapi.domain.Loan;
import ru.popov.loanrestapi.domain.Person;
import ru.popov.loanrestapi.repositories.BlacklistRepository;
import ru.popov.loanrestapi.repositories.CountryRepository;
import ru.popov.loanrestapi.repositories.LoanRepository;
import ru.popov.loanrestapi.repositories.PersonRepository;
import ru.popov.loanrestapi.util.exceptions.CountryNotFoundException;
import ru.popov.loanrestapi.util.exceptions.LoanNotFoundException;
import ru.popov.loanrestapi.util.exceptions.PersonIsInBlacklistException;
import ru.popov.loanrestapi.util.exceptions.PersonNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    private LoanService loanService;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private BlacklistRepository blacklistRepository;

    @BeforeEach
    void setUp() {
        loanService = new LoanServiceImpl(
                loanRepository,
                personRepository,
                countryRepository,
                blacklistRepository
        );
    }

    @Test
    @DisplayName("It should show all loans")
    void itShouldGetAllLoans() {
        // given
        Person person = new Person("Dmitry", "Popov");
        Country country = new Country("USA");
        Loan loan = new Loan(
                2500.0,
                LocalDate.of(2022, 11, 5),
                false, person, country);
        given(loanRepository.findAll()).willReturn(List.of(loan));

        // when
        List<Loan> loans = loanService.getAll();

        // then
        assertThat(loans.contains(loan)).isTrue();
    }

    @Test
    @DisplayName("It shouldn't show all loans (Loans not found exception)")
    void itShouldNotGetAllLoansAndThrowLoanNotFoundException() {
        // given
        given(loanRepository.findAll()).willReturn(List.of());

        // when
        // then
        Assertions.assertThatThrownBy(() -> loanService.getAll())
                .isInstanceOf(LoanNotFoundException.class)
                .hasMessageContaining("We have no loans atm");
    }

    @Test
    @DisplayName("It should add loan")
    void itShouldAddLoan() {
        // given
        String personName = "Dmitry";
        Person person = new Person(personName, "Popov");
        String countryName = "USA";
        Country country = new Country(countryName);
        Loan loan = new Loan(
                2500.0,
                LocalDate.of(2022, 11, 5),
                false, person, country);
        given(personRepository.findByName(personName)).willReturn(Optional.of(person));
        given(blacklistRepository.findByPersonId(person.getId())).willReturn(Optional.empty());
        given(countryRepository.findByName(countryName)).willReturn(Optional.of(country));

        // when
        loanService.addLoan(loan);

        // then
        ArgumentCaptor<Loan> captor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(captor.capture());
        Loan capturedLoan = captor.getValue();
        assertThat(loan).isEqualTo(capturedLoan);
    }

    @Test
    @DisplayName("It shouldn't add loan (Person not found exception)")
    void itShouldNotAddLoanAndThrowPersonNotFoundException() {
        // given
        String personName = "Dmitry";
        Person person = new Person(personName, "Popov");
        String countryName = "USA";
        Country country = new Country(countryName);
        Loan loan = new Loan(
                2500.0,
                LocalDate.of(2022, 11, 5),
                false, person, country);
        given(personRepository.findByName(personName)).willReturn(Optional.empty());
//        given(blacklistRepository.findByPersonId(person.getId())).willReturn(Optional.empty());
//        given(countryRepository.findByName(countryName)).willReturn(Optional.of(country));

        // when
        // then
        Assertions.assertThatThrownBy(() -> loanService.addLoan(loan))
                .isInstanceOf(PersonNotFoundException.class)
                .hasMessageContaining("person with name: " + person.getName() + " does not exist");
        verify(loanRepository, never()).save(loan);
    }

    @Test
    @DisplayName("It shouldn't add loan (Person is in blacklist exception)")
    void itShouldNotAddLoanAndThrowPersonIsInBlacklistException() {
        // given
        String personName = "Dmitry";
        Person person = new Person(personName, "Popov");
        String countryName = "USA";
        Country country = new Country(countryName);
        Loan loan = new Loan(
                2500.0,
                LocalDate.of(2022, 11, 5),
                false, person, country);
        Blacklist blacklist = new Blacklist(person);
        given(personRepository.findByName(personName)).willReturn(Optional.of(person));
        given(blacklistRepository.findByPersonId(person.getId())).willReturn(Optional.of(blacklist));
//        given(countryRepository.findByName(countryName)).willReturn(Optional.of(country));

        // when
        // then
        Assertions.assertThatThrownBy(() -> loanService.addLoan(loan))
                .isInstanceOf(PersonIsInBlacklistException.class)
                .hasMessageContaining("person with name: " + person.getName() + " is in blacklist");
        verify(loanRepository, never()).save(loan);
    }

    @Test
    @DisplayName("It shouldn't add loan (Country not found exception)")
    void itShouldNotAddLoanAndThrowCountryNotFoundException() {
        // given
        String personName = "Dmitry";
        Person person = new Person(personName, "Popov");
        String countryName = "USA";
        Country country = new Country(countryName);
        Loan loan = new Loan(
                2500.0,
                LocalDate.of(2022, 11, 5),
                false, person, country);
        given(personRepository.findByName(personName)).willReturn(Optional.of(person));
//        given(blacklistRepository.findByPersonId(person.getId())).willReturn(Optional.empty());
        given(countryRepository.findByName(countryName)).willReturn(Optional.empty());

        // when
        // then
        Assertions.assertThatThrownBy(() -> loanService.addLoan(loan))
                .isInstanceOf(CountryNotFoundException.class)
                .hasMessageContaining("country with name: " + country.getName() + " does not exist");
        verify(loanRepository, never()).save(loan);
    }

    @Test
    @DisplayName("It should show all approved loans")
    void itShouldGetAllApprovedLoans() {
        // given
        Person person = new Person("Dmitry", "Popov");
        Country country = new Country("USA");
        Loan loan = new Loan(
                2500.0,
                LocalDate.of(2022, 11, 5),
                true, person, country);
        given(loanRepository.findByApprovedIsTrue()).willReturn(List.of(loan));

        // when
        List<Loan> approvedLoans = loanService.getAllApprovedLoans();

        // then
        assertThat(approvedLoans.contains(loan)).isTrue();
    }

    @Test
    @DisplayName("It shouldn't show all approved loans (Loan not found exception)")
    void itShouldNotGetAllApprovedLoansAndThrowLoanNotFoundException() {
        // given
        given(loanRepository.findByApprovedIsTrue()).willReturn(List.of());

        // when
        // then
        Assertions.assertThatThrownBy(() -> loanService.getAllApprovedLoans())
                .isInstanceOf(LoanNotFoundException.class)
                .hasMessageContaining("We have no approved loans atm");
    }

    @Test
    @DisplayName("It should show all persons approved loans")
    void itShouldGetAllApprovedLoansByPerson() {
        // given
        String name = "Dmitry";
        Person person = new Person(name, "Popov");
        Country country = new Country("USA");
        Loan loan = new Loan(
                2500.0,
                LocalDate.of(2022, 11, 5),
                true, person, country);
        given(personRepository.findByName(name)).willReturn(Optional.of(person));
        given(loanRepository.findByPersonIdAndApprovedIsTrue(person.getId())).willReturn(List.of(loan));

        // when
        List<Loan> approvedLoans = loanService.getAllApprovedLoansByPerson(name);

        // then
        assertThat(approvedLoans.contains(loan)).isTrue();
    }

    @Test
    @DisplayName("It shouldn't show all persons approved loans (Person not found exception)")
    void itShouldNotGetAllApprovedLoansByPersonAndThrowPersonNotFoundException() {
        // given
        String name = "Dmitry";
        Person person = new Person(name, "Popov");
//        given(loanRepository.findByPersonIdAndApprovedIsTrue(person.getId())).willReturn(List.of(loan));

        // when
        // then
        Assertions.assertThatThrownBy(() -> loanService.getAllApprovedLoansByPerson(person.getName()))
                .isInstanceOf(PersonNotFoundException.class)
                .hasMessageContaining("person with name: " + person.getName() + " does not exist");
        verify(loanRepository, never()).findByPersonIdAndApprovedIsTrue(person.getId());
    }

    @Test
    @DisplayName("It shouldn't show all persons approved loans (Loan not found exception)")
    void itShouldNotGetAllApprovedLoansByPersonAndThrowLoanNotFoundException() {
        // given
        String name = "Dmitry";
        Person person = new Person(name, "Popov");
        given(personRepository.findByName(name)).willReturn(Optional.of(person));
        given(loanRepository.findByPersonIdAndApprovedIsTrue(person.getId())).willReturn(List.of());

        // when
        // then
        Assertions.assertThatThrownBy(() -> loanService.getAllApprovedLoansByPerson(person.getName()))
                .isInstanceOf(LoanNotFoundException.class)
                .hasMessageContaining("person with name: " + name + " doesn't has any approved loans");
    }

    @Test
    @DisplayName("It should approved loan by id")
    void itShouldApprovedLoanById() {
        // given
        String name = "Dmitry";
        Person person = new Person(name, "Popov");
        Country country = new Country("USA");
        Loan loan = new Loan(
                2500.0,
                LocalDate.of(2022, 11, 5),
                true, person, country);
        given(loanRepository.findById(loan.getId())).willReturn(Optional.of(loan));

        // when
        loanService.approvedLoanById(loan.getId());

        // then
        ArgumentCaptor<Loan> captor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(captor.capture());
        Loan capturedLoan = captor.getValue();
        assertThat(loan).isEqualTo(capturedLoan);
    }

    @Test
    @DisplayName("It shouldn't approved loan by id (Loan not found exception)")
    void itShouldNotApprovedLoanByIdAndThrowLoanNotFoundException() {
        // given
        String name = "Dmitry";
        Person person = new Person(name, "Popov");
        Country country = new Country("USA");
        Loan loan = new Loan(
                2500.0,
                LocalDate.of(2022, 11, 5),
                true, person, country);
        given(loanRepository.findById(loan.getId())).willReturn(Optional.empty());

        // when
        // then
        Assertions.assertThatThrownBy(() -> loanService.approvedLoanById(loan.getId()))
                .isInstanceOf(LoanNotFoundException.class)
                .hasMessageContaining("loan with id: " + loan.getId() + " does not exist");
        verify(loanRepository, never()).save(loan);
    }
}