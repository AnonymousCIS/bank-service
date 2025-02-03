package org.anonymous.bank.services.transaction;

import org.anonymous.bank.controllers.TransactionSearch;
import org.anonymous.bank.entities.Bank;
import org.anonymous.bank.entities.Transaction;
import org.anonymous.global.paging.ListData;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class TransactionInfoService {

    /**
     * 거래 내역 단일 조회
     *
     * @param seq
     * @return
     */
    public Transaction get(Long seq) {

        return null;
    }

    /**
     * 거래 내역 목록 조회
     *
     * @param search
     * @return
     */
    public ListData<Transaction> getList(TransactionSearch search) {

        return null;
    }

    /**
     * 공통 처리
     *
     * @param item
     */
    public void addInfo(Bank item) {

    }
}
