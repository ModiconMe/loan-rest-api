package ru.popov.loanrestapi.services;

import ru.popov.loanrestapi.domain.Loan;
import ru.popov.loanrestapi.util.exceptions.CountryNotFoundException;
import ru.popov.loanrestapi.util.exceptions.LoanNotFoundException;
import ru.popov.loanrestapi.util.exceptions.PersonNotFoundException;

import java.util.List;

public interface LoanService {

    Loan addLoan(Loan loan) throws PersonNotFoundException, CountryNotFoundException;

    List<Loan> getAll();

    List<Loan> getAllApprovedLoans();

    List<Loan> getAllApprovedLoansByPerson(String name) throws LoanNotFoundException;

    void approvedLoanById(int id);
}
