package org.anonymous.bank.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "AdminBank", description = "금융 관리 API")
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
    @Operation(summary = "계좌 단일 수정 처리", description = "계좌를 단일로 수정합니다.")
    @Parameters({
            @Parameter(name = "form", description = "계좌 작성 양식"),
            @Parameter(name = "mode", description = "계좌 처리 모드", examples = {
                    @ExampleObject(name = "add", value = "add"),
                    @ExampleObject(name = "edit", value = "edit")
            }),
            @Parameter(name = "name", description = "예금주", required = true, example = "김철수"),
            @Parameter(name = "password", description = "계좌 비밀 번호", required = true, example = "1125")
    })
    @PatchMapping("/edit")
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
    @Operation(summary = "계좌 단일 & 목록 일괄 삭제 처리", description = "계좌 ID로 계좌를 단일 & 목록 일괄 삭제합니다.")
    @Parameter(name = "seq", description = "계좌 ID", required = true, example = "1125")
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
    @Operation(summary = "거래 내역 단일 & 목록 일괄 삭제 처리", description = "거래 내역 ID로 거래 내역을 단일 & 목록 일괄 삭제합니다.")
    @Parameter(name = "seq", description = "거래 내역 ID", required = true, example = "1125")
    @DeleteMapping("/transaction/deletes")
    public JSONData transactionDeletes(@RequestParam("seq") List<Long> seqs) {
        List<Transaction> data = transactionDeleteService.deletes(seqs);
        return new JSONData(data);
    }
}