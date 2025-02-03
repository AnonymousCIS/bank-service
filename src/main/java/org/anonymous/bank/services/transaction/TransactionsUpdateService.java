package org.anonymous.bank.services.transaction;

import org.anonymous.bank.controllers.RequestTransaction;
import org.anonymous.bank.entities.Transaction;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class TransactionsUpdateService {

    /**
     * 거래내역 등록 | 수정
     *
     * @param form
     * @return
     */
    public Transaction process(RequestTransaction form) {

        return null;
    }
}
