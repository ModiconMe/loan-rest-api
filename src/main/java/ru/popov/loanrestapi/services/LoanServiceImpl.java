package ru.popov.loanrestapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final PersonRepository personRepository;
    private final CountryRepository countryRepository;
    private final BlacklistRepository blacklistRepository;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, PersonRepository personRepository, CountryRepository countryRepository, BlacklistRepository blacklistRepository) {
        this.loanRepository = loanRepository;
        this.personRepository = personRepository;
        this.countryRepository = countryRepository;
        this.blacklistRepository = blacklistRepository;
    }

    @Override
    public List<Loan> getAll() {
        List<Loan> loans = loanRepository.findAll();

        if (loans.isEmpty())
            throw new LoanNotFoundException("We have no loans atm");

        return loans;
    }

    @Override
    @Transactional
    public Loan addLoan(Loan loan) throws PersonNotFoundException, CountryNotFoundException {
        Person loanPerson = loan.getPerson();
        String loanPersonName = loanPerson.getName();

        // check if person does not exist
        Optional<Person> optionalPerson = personRepository.findByName(loanPersonName);
        if (optionalPerson.isEmpty())
            throw new PersonNotFoundException("person with name: " + loanPersonName + " does not exist");
        Person person = optionalPerson.get();

        // check if person is in blacklist
        Optional<Blacklist> optionalBlacklistPerson = blacklistRepository.findByPersonId(person.getId());
        if (optionalBlacklistPerson.isPresent())
            throw new PersonIsInBlacklistException("person with name: " + loanPersonName + " is in blacklist");

        // check if country does not exist
        Optional<Country> optionalCountry = countryRepository.findByName(loan.getCountry().getName());
        if (optionalCountry.isEmpty())
            throw new CountryNotFoundException("country with name: " + loan.getCountry().getName() + " does not exist");
        Country country = optionalCountry.get();

        // set person and country
        loan.setPerson(person);
        loan.setCountry(country);
        loanRepository.save(loan);
        return loan;
    }

    @Override
    public List<Loan> getAllApprovedLoans() {
        List<Loan> approvedLoans = loanRepository.findByApprovedIsTrue();

        if (approvedLoans.isEmpty())
            throw new LoanNotFoundException("We have no approved loans atm");

        return approvedLoans;
    }

    @Override
    public List<Loan> getAllApprovedLoansByPerson(String name) throws LoanNotFoundException {
        Optional<Person> optionalPerson = personRepository.findByName(name);

        if (optionalPerson.isEmpty())
            throw new PersonNotFoundException("person with name: " + name + " does not exist");

        List<Loan> approvedLoans = loanRepository.findByPersonIdAndApprovedIsTrue(optionalPerson.get().getId());

        if (approvedLoans.isEmpty())
            throw new LoanNotFoundException("person with name: " + name + " doesn't has any approved loans");

        return approvedLoans;
    }

    @Override
    @Transactional
    public void approvedLoanById(int id) {
        Optional<Loan> optionalLoan = loanRepository.findById(id);

        if (optionalLoan.isEmpty())
            throw new LoanNotFoundException("loan with id: " + id + " does not exist");

        Loan loan = optionalLoan.get();
        loan.setApproved(true);
        loanRepository.save(loan);
    }
}
