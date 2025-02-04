package org.anonymous.bank.services.transaction;

import lombok.RequiredArgsConstructor;
import org.anonymous.bank.controllers.RequestTransaction;
import org.anonymous.bank.entities.Bank;
import org.anonymous.bank.entities.Transaction;
import org.anonymous.bank.exceptions.BankNotFountException;
import org.anonymous.bank.repositories.BankRepository;
import org.anonymous.bank.repositories.TransactionRepository;
import org.anonymous.bank.services.BankInfoService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Lazy
@Service
@RequiredArgsConstructor
public class TransactionsUpdateService {


    private final BankInfoService bankInfoService;
    private final TransactionRepository transactionRepository;
    private final BankRepository bankRepository;

    /**
     * 거래내역 등록
     *
     * @param form
     * @return
     */
    public Transaction process(RequestTransaction form) {

        Bank bank = bankInfoService.get(form.getBank().getSeq());

        if (bank == null) {
            throw new BankNotFountException();
        }

        Transaction transaction = new Transaction();

        transaction.setBank(bank);
        transaction.setPayAmount(form.getPayAmount());

        transactionRepository.saveAndFlush(transaction);
        return transaction;
    }

    /**
     * 거래내역 다
     */
    @Scheduled(cron="0 0 0 * * *")
    public void updateTransactions() {
        List<Bank> banks = bankRepository.findAll();

        if (banks.isEmpty()) {
            return;
        }

        Transaction transaction = new Transaction();
        List<Transaction> transactions = new ArrayList<>();
        Random random = new Random();
        for (Bank bank : banks) {
            for (int i = 0; i < random.nextInt(10); i++) {
                transaction.setBank(bank);
                transaction.setPayAmount(random.nextLong(1000) * 300L);
                transactions.add(transaction);
            }
        }

        transactionRepository.saveAllAndFlush(transactions);
    }
}


















