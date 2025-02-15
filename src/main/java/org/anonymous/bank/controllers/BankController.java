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
@Tag(name = "Bank", description = "계좌 관리 API")
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
    @Operation(summary = "계좌 등록 처리", description = "신규 계좌를 등록합니다.")
    @Parameters({
            @Parameter(name = "form", description = "계좌 작성 양식"),
            @Parameter(name = "mode", description = "계좌 처리 모드", examples = {
                    @ExampleObject(name = "add", value = "add"),
                    @ExampleObject(name = "edit", value = "edit")
            }),
            @Parameter(name = "bankName", description = "은행명", required = true, examples = {
                    @ExampleObject(name = "한국은행", value = "HANKUK"),
                    @ExampleObject(name = "국민은행", value = "KB"),
                    @ExampleObject(name = "제일은행", value = "SC"),
                    @ExampleObject(name = "한국시티은행", value = "CITY"),
                    @ExampleObject(name = "하나은행", value = "HANA"),
                    @ExampleObject(name = "신한은행", value = "SHINHAN"),
                    @ExampleObject(name = "K-뱅크", value = "KBANK"),
                    @ExampleObject(name = "카카오은행", value = "KAKAO"),
                    @ExampleObject(name = "토스은행", value = "TOSS"),
                    @ExampleObject(name = "수협은행", value = "SUHYUP"),
                    @ExampleObject(name = "부산은행", value = "BUSAN"),
                    @ExampleObject(name = "경남은행", value = "KYUNGNAM"),
                    @ExampleObject(name = "광주은행", value = "KYANGJOO"),
                    @ExampleObject(name = "전북은행", value = "JUNBOK"),
                    @ExampleObject(name = "제주은행", value = "JEJOO"),
                    @ExampleObject(name = "롯데카드", value = "LOTTE"),
                    @ExampleObject(name = "농협은행", value = "NONGHYUP"),
                    @ExampleObject(name = "삼성카드", value = "SAMSUNG"),
                    @ExampleObject(name = "현대카드", value = "HYUNDAI"),
                    @ExampleObject(name = "우리은행", value = "WOORI"),
                    @ExampleObject(name = "신협은행", value = "SINHYUP"),
                    @ExampleObject(name = "새마을금고", value = "SAEMAEULGEUMGO"),
                    @ExampleObject(name = "우체국", value = "WOOCAEKUK")
            }),
            @Parameter(name = "accountNumber", description = "계좌 번호", required = true, example = "1234-12-123456"),
            @Parameter(name = "name", description = "예금주", required = true, example = "김철수"),
            @Parameter(name = "password", description = "계좌 비밀 번호", required = true, example = "1125")
    })
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
    @Operation(summary = "계좌 단일 조회", description = "계좌 ID로 계좌를 검색해 단일 조회합니다.")
    @Parameter(name = "seq", description = "계좌 ID")
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
    @Operation(summary = "계좌 목록 조회", description = "계좌 목록을 검색해 조회합니다.")
    @Parameters({
            @Parameter(name = "search", description = "계좌 목록 조회용"),
            @Parameter(name = "sort", description = "필드명_정렬방향, 검색 처리시 분해해서 사용", example = "createdAt_DESC"),
            @Parameter(name = "email", description = "회원 이메일별 조회용"),
            @Parameter(name = "bankName", description = "은행명별 조회용"),
    })
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
    @Operation(summary = "계좌 삭제 단일 & 목록 일괄 처리", description = "계좌 ID로 댓글을 단일 & 목록 삭제합니다. 일반 사용자용 삭제이므로 DB에서 삭제되지 않고 DeletedAt을 현재 시간으로 부여합니다.")
    @Parameter(name = "seq", description = "계좌 ID")
    @PatchMapping("/userdeletes")
    public JSONData userDeletes(@RequestParam("seq")List<Long> seqs) {

        List<Bank> data = deleteService.userDeletes(seqs);

        return new JSONData(data);
    }

    // 1. 거래내역 인위적 추가 C
    // 2. 거래내역 단일, 목록 조회 R -> 자기껏만. 목록에서 sDate, eDate loe, goe
    // 3. 거래내역이 자동적으로 생성되게 할거에요. -> 스케줄써서. 하루에 5~10개씩.
    // 4. 카드 추천. -> 연회비, 카드종류 2개만 선택, 그리고 bankName 자동으로. 한도는 얘가 썼던 금액을 보고. 카테고리는 자동으로.
    // 5. 대출 추천. -> 한도는 얘가 썼던 금액을 보고, bankName 자동, category 선택, 이자율 랜덤, 상환날짜 랜덤.

    /**
     * 거래내역 생성
     * @param form
     * @param errors
     * @return
     */
    @Operation(summary = "거래 내역 생성", description = "신규 거래 내역을 생성합니다.")
    @Parameters({
            @Parameter(name = "form", description = "거래 내역 양식"),
            @Parameter(name = "payAmount", description = "거래 금액", required = true, example = "20000"),
            @Parameter(name = "bank", description = "결제 계좌", required = true)
    })
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

    @Operation(summary = "거래 내역 단일 조회", description = "거래 내역 ID로 거래 내역을 검색해 단일 조회합니다.")
    @Parameter(name = "seq", description = "거래 내역 ID")
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
    @Operation(summary = "거래 내역 목록 조회", description = "거래 내역 목록을 검색해 조회합니다.")
    @Parameters({
            @Parameter(name = "search", description = "거래 내역 목록 조회용"),
            @Parameter(name = "payAmountMin", description = "가장 거래 금액이 낮은 조회용"),
            @Parameter(name = "payAmountMax", description = "가장 거래 금액이 높은 조회용"),
    })
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
    @Operation(summary = "카드 추천", description = "사용자의 거래 내역을 바탕으로 카드를 추천합니다.")
    @Parameters({
            @Parameter(name = "form", description = "카드 추천 작성 양식"),
            @Parameter(name = "annualFee", description = "연회비", required = true),
            @Parameter(name = "cardType", description = "카드 종류", required = true, examples = {
                    @ExampleObject(name = "개인체크", value = "PersonalCheck"),
                    @ExampleObject(name = "개인신용", value = "PersonalCredit"),
                    @ExampleObject(name = "법인체크", value = "CorporateCheck"),
                    @ExampleObject(name = "법인신용", value = "CorporateCredit"),
            })
    })
    @PostMapping("/transaction/card")
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
    @Operation(summary = "대출 추천", description = "사용자의 거래 내역을 바탕으로 대출을 추천합니다.")
    @Parameters({
            @Parameter(name = "form", description = "대출 추천 작성 양식"),
            @Parameter(name = "category", description = "대출 종류", required = true, examples = {
                    @ExampleObject(name = "신용대출", value = "CREDITLOAN"),
                    @ExampleObject(name = "담보대출", value = "MORTGAGELOAN"),
            })
    })
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
