package org.anonymous.bank.services;

import org.anonymous.bank.controllers.BankSearch;
import org.anonymous.bank.entities.Bank;
import org.anonymous.global.paging.ListData;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class BankInfoService {

    /**
     * 계좌 단일 조회
     *
     * @param seq
     * @return
     */
    public Bank get(Long seq) {

        return null;
    }

    /**
     * 계좌 목록 조회
     *
     * @param search
     * @return
     */
    public ListData<Bank> getList(BankSearch search) {

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