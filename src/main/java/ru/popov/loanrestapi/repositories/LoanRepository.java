package ru.popov.loanrestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.popov.loanrestapi.domain.Loan;

import java.util.List;

/**
 * Добавлен метод для поиска всех одобренных займов
 * Добавлени метод для поиска всех одобренный займов у человека
 */
@Repository
public interface LoanRepository  extends JpaRepository<Loan, Integer> {

    List<Loan> findByApprovedIsTrue();
    List<Loan> findByPersonIdAndApprovedIsTrue(int personId);
}
