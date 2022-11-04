package ru.popov.loanrestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.popov.loanrestapi.domain.Loan;

import java.util.List;

@Repository
public interface LoanRepository  extends JpaRepository<Loan, Integer> {

    List<Loan> findByApprovedIsTrue();
    List<Loan> findByPersonIdAndApprovedIsTrue(int personId);
}
