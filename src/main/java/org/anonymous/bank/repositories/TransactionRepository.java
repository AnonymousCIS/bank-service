package org.anonymous.bank.repositories;

import org.anonymous.bank.entities.QTransaction;
import org.anonymous.bank.entities.Transaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, QuerydslPredicateExecutor<Transaction> {
    @EntityGraph(attributePaths = "bank")
    Optional<Transaction> findBySeq(Long seq);

    @EntityGraph(attributePaths = "bank")
    List<Transaction> findAllByCreatedBy(String email);
}
