package org.anonymous.bank.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.bank.entities.Bank;
import org.anonymous.bank.services.BankDeleteService;
import org.anonymous.bank.services.BankInfoService;
import org.anonymous.bank.services.BankUpdateService;
import org.anonymous.bank.validators.BankValidator;
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

    private final BankValidator validator;

    private final BankInfoService infoService;

    private final BankUpdateService updateService;

    private final BankDeleteService deleteService;

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
