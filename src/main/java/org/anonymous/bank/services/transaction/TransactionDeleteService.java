package org.anonymous.bank.services.transaction;

import lombok.RequiredArgsConstructor;
import org.anonymous.bank.entities.Bank;
import org.anonymous.bank.entities.Transaction;
import org.anonymous.bank.exceptions.BankNotFountException;
import org.anonymous.bank.repositories.TransactionRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class TransactionDeleteService {

    private final TransactionInfoService transactionInfoService;
    private final TransactionRepository repository;

    /**
     * 거래 내역 단일 삭제
     *
     * Base Method
     *
     * @param seq
     * @return
     */
    public Transaction delete(Long seq) {

        Transaction item = transactionInfoService.get(seq);

        if (item == null) throw new BankNotFountException();

        repository.delete(item);

        repository.flush();

        return item;
    }

    /**
     * 거래 내역 목록 삭제
     *
     * @param seqs
     * @return
     */
    public List<Transaction> deletes(List<Long> seqs) {

        List<Transaction> deleted = new ArrayList<>();

        for (Long seq : seqs) {

            Transaction item = delete(seq);

            if (item != null) {

                deleted.add(item);
            }
        }

        return deleted;
    }
}
