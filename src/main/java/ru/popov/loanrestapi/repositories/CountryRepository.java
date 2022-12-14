package ru.popov.loanrestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.popov.loanrestapi.domain.Country;

import java.util.Optional;

/**
 * Добавлен метод для поиска страны по названию
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    Optional<Country> findByName(String name);
}
