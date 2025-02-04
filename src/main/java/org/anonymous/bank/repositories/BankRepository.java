package org.anonymous.bank.repositories;

import org.anonymous.bank.entities.Bank;
import org.anonymous.bank.entities.Transaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long>, QuerydslPredicateExecutor<Bank> {
    @EntityGraph(attributePaths = "transactions")
    Optional<Bank> findBySeq(Long seq);
}
