package ru.popov.loanrestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.popov.loanrestapi.domain.Blacklist;

import java.util.Optional;

/**
 * Добавлен метод для проверки человека (находится ли он в черном списке)
 * Добавлен метод для удаления человека из черного списка по id человека
 */
@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Integer> {
    Optional<Blacklist> findByPersonId(int personId);
    void deleteByPersonId(int personId);
}
