package org.anonymous.bank.services.transaction;

import org.anonymous.bank.entities.Transaction;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Service
public class TransactionDeleteService {

    /**
     * 거래 내역 단일 삭제
     *
     * Base Method
     *
     * @param seq
     * @return
     */
    public Transaction delete(Long seq) {

        return null;
    }

    /**
     * 거래 내역 목록 삭제
     *
     * @param seqs
     * @return
     */
    public List<Transaction> deletes(List<Long> seqs) {

        return null;
    }
}
