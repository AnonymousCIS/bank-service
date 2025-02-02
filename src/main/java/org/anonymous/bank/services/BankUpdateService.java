package org.anonymous.bank.services;

import org.anonymous.bank.controllers.RequestBank;
import org.anonymous.bank.entities.Bank;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class BankUpdateService {

    /**
     * 계좌 등록 | 수정
     *
     * @param form
     * @return
     */
    public Bank process(RequestBank form) {

        return null;
    }
}