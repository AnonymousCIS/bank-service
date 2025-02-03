package org.anonymous.bank.repositories;

import org.anonymous.bank.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BankRepository extends JpaRepository<Bank, Long>, QuerydslPredicateExecutor<Bank> {
}
