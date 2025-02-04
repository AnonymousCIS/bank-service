package org.anonymous.bank.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.bank.entities.Transaction;
import org.anonymous.bank.services.transaction.TransactionInfoService;
import org.anonymous.bank.services.transaction.TransactionsUpdateService;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.rests.JSONData;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

/**
 * 거래 내역 공용 기능
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final Utils utils;

    private final TransactionsUpdateService updateService;

    private final TransactionInfoService infosService;

    /**
     * 거래 내역 등록 처리
     *
     * @return
     */
    @PostMapping("/create")
    public JSONData create(@Valid @RequestBody RequestTransaction form, Errors errors) {

        if (errors.hasErrors()) throw new BadRequestException(utils.getErrorMessages(errors));

        Transaction data =  updateService.process(form);

        return new JSONData(data);
    }

    /**
     * 거래 내역 단일 조회
     *
     * @param seq
     * @return
     */
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {

        Transaction data = infosService.get(seq);

        return new JSONData(data);
    }

    /**
     * 거래 내역 목록 조회
     *
     * @return
     */
    @GetMapping("/list")
    public JSONData list(@ModelAttribute TransactionSearch search) {

        ListData<Transaction> data = infosService.getList(search);

        return new JSONData(data);
    }
}
