package ru.popov.loanrestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.popov.loanrestapi.domain.Blacklist;

import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Integer> {
    Optional<Blacklist> findByPersonId(int personId);
    void deleteByPersonId(int personId);
}
