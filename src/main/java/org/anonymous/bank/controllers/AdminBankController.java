package org.anonymous.bank.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.bank.entities.Bank;
import org.anonymous.bank.entities.Transaction;
import org.anonymous.bank.services.BankDeleteService;
import org.anonymous.bank.services.BankUpdateService;
import org.anonymous.bank.services.transaction.TransactionDeleteService;
import org.anonymous.bank.validators.BankValidator;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.rests.JSONData;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자용 기능
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminBankController {

    private final Utils utils;

    private final BankValidator validator;

    private final BankUpdateService updateService;

    private final BankDeleteService bankDeleteService;

    private final TransactionDeleteService transactionDeleteService;

    /**
     * 계좌 단일 수정 처리
     *
     * @return
     */
    @PatchMapping("/bank/edit")
    public JSONData edit(@Valid @RequestBody RequestBank form, Errors errors) {

        validator.validate(form, errors);

        if (errors.hasErrors()) throw new BadRequestException(utils.getErrorMessages(errors));

        Bank data = updateService.process(form);

        return new JSONData(data);
    }

    /**
     * 계좌 단일 | 목록 일괄 삭제 처리
     *
     * @return
     */
    @DeleteMapping("/deletes")
    public JSONData deletes(@RequestParam("seq") List<Long> seqs) {

        List<Bank> data = bankDeleteService.deletes(seqs);

        return new JSONData(data);
    }

    /**
     * 거래 내역 단일 | 목록 일괄 삭제 처리
     *
     * @return
     */
    @DeleteMapping("/transaction/deletes")
    public JSONData transactionDeletes(@RequestParam("seq") List<Long> seqs) {

        List<Transaction> data = transactionDeleteService.deletes(seqs);

        return new JSONData(data);
    }
}