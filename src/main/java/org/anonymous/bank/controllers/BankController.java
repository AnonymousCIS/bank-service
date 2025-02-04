package org.anonymous.bank.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.bank.entities.Bank;
import org.anonymous.bank.entities.Transaction;
import org.anonymous.bank.services.BankDeleteService;
import org.anonymous.bank.services.BankInfoService;
import org.anonymous.bank.services.BankUpdateService;
import org.anonymous.bank.services.RecommendService;
import org.anonymous.bank.services.transaction.TransactionInfoService;
import org.anonymous.bank.services.transaction.TransactionsUpdateService;
import org.anonymous.bank.validators.BankValidator;
import org.anonymous.bank.validators.CardValidator;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.rests.JSONData;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 계좌 공용 기능
 *
 */
@RestController
@RequiredArgsConstructor
public class BankController {

    private final Utils utils;


    // region Bank Service

    private final BankValidator validator;

    private final BankInfoService infoService;

    private final BankUpdateService updateService;

    private final BankDeleteService deleteService;

    // endregion

    // region Transaction Service

    private final TransactionsUpdateService transactionsUpdateService;
    private final TransactionInfoService transactionInfoService;
    private final CardValidator cardValidator;

    // endregion

    // region 추천 service

    private final RecommendService recommendService;

    // endregion

    /**
     * 계좌 등록 처리
     *
     * @param form
     * @return
     */
    @PostMapping("/create")
    public JSONData create(@Valid @RequestBody RequestBank form, Errors errors) {

        validator.validate(form, errors);

        if (errors.hasErrors()) throw new BadRequestException(utils.getErrorMessages(errors));

        Bank data = updateService.process(form);

        return new JSONData(data);
    }

    /**
     * 계좌 단일 조회
     *
     * @param seq
     * @return
     */
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {

        Bank data = infoService.get(seq);

        return new JSONData(data);
    }

    /**
     * 계좌 목록 조회
     *
     * @return
     */
    @GetMapping("/list")
    public JSONData list(@ModelAttribute BankSearch search) {

        ListData<Bank> data = infoService.getList(search);

        return new JSONData(data);
    }

    /**
     * 계좌 등록 삭제
     *
     * 단일 | 목록 일괄 수정
     *
     * DB 삭제 X, 일반 유저 전용 삭제로 deletedAt 현재 시간으로 부여
     *
     * @return
     */
    @PatchMapping("/userdeletes")
    public JSONData userDeletes(@RequestParam("seq")List<Long> seqs) {

        List<Bank> data = deleteService.userDeletes(seqs);

        return new JSONData(data);
    }

    // 1. 거래내역 인위적 추가 C
    // 2. 거래내역 단일, 목록 조회 R -> 자기껏만. 목록에서 sDate, eDate loe, goe
    // 3. 거래내역이 자동적으로 생성되게 할거에요. -> 이새끼 스케줄써서. 하루에 5~10개씩.
    // 4. 카드 추천. -> 연회비, 카드종류 2개만 선택, 그리고 bankName 자동으로. 한도는 얘가 썼던 금액을 보고. 카테고리는 자동으로.
    // 5. 대출 추천. -> 한도는 얘가 썼던 금액을 보고, bankName 자동, category 선택, 이자율 랜덤, 상환날짜 랜덤.

    /**
     * 거래내역 생성
     * @param form
     * @param errors
     * @return
     */
    @PostMapping("/transaction/create")
    public JSONData transactionCreate(@Valid @RequestBody RequestTransaction form, Errors errors) {
        if (errors.hasErrors()) throw new BadRequestException(utils.getErrorMessages(errors));
        Transaction transaction = transactionsUpdateService.process(form);
        return new JSONData(transaction);
    }

    /**
     * 거래내역 단일 조회
     * @param seq
     * @return
     */
    @GetMapping("/transaction/info/{seq}")
    public JSONData transactionInfo(@PathVariable("seq") Long seq) {
        Transaction transaction = transactionInfoService.get(seq);
        return new JSONData(transaction);
    }

    /**
     * 거래내역 목록 조회
     * @param search
     * @return
     */
    @GetMapping("/transaction/list")
    public JSONData transactionList(@ModelAttribute TransactionSearch search) {
        ListData<Transaction> listData = transactionInfoService.getList(search);
        return new JSONData(listData);
    }

    /**
     * 카드 추천
     * @param form
     * @param errors
     * @return
     */
    @GetMapping("/transaction/card")
    public JSONData transactionCard(@Valid @RequestBody RequestCard form, Errors errors) {

        cardValidator.validate(form, errors);

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        // 추천 시스템 넣으면 될거같다.

        String data = recommendService.recommendCard(form);

        return new JSONData(data);
    }

    /**
     * 대출 추천
     * @param form
     * @param errors
     * @return
     */
    @GetMapping("/transaction/loan")
    public JSONData transactionLoan(@Valid @RequestBody RequestLoan form, Errors errors) {

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        // 추천 시스템 넣으면 될거같다.
        String data = recommendService.recommendLoan(form);

        return new JSONData(data);
    }

//    /**
//     * 계좌 번호로 개별 공통 처리
//     *
//     * Base Method
//     *
//     * @param seq
//     * @param mode
//     */
//    private void commonProcess(Long seq, String mode) {
//
//
//    }
}
