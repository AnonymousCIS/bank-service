package org.anonymous.bank.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.bank.entities.Transaction;
import org.anonymous.bank.services.transaction.TransactionInfoService;
import org.anonymous.bank.services.transaction.TransactionsUpdateService;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.rests.JSONData;
import org.anonymous.member.MemberUtil;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

/**
 * 거래 내역 공용 기능
 *
 */
@Tag(name = "Transaction", description = "거래내역 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final Utils utils;

    private final TransactionsUpdateService updateService;

    private final TransactionInfoService infoService;

    private final MemberUtil memberUtil;

    /**
     * 거래 내역 등록 처리
     *
     * @return
     */
    @Operation(summary = "거래 내역 등록 처리", method="POST", description = "거래 내역 등록을 처리합니다.")
    @ApiResponse(responseCode = "200")
    @Parameters({
            @Parameter(name="payAmount", description = "거래 금액"),
            @Parameter(name="Bank", description = "자신의 계좌"),
    })
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
    @Operation(summary = "거래 내역 단일 조회", method="Get", description = "거래 내역을 단일로 조회합니다.")
    @ApiResponse(responseCode = "200")
    @Parameter(name="seq", description = "거래 내역 번호")
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {

        Transaction data = infoService.get(seq);

        return new JSONData(data);
    }

    /**
     * 거래 내역 목록 조회
     *
     * @return
     */
    @Operation(summary = "거래 내역 목록 조회", method="GET", description = "data - 조회된 거래목록, pagination - 페이징 기초 데이터")
    @ApiResponse(responseCode = "200")
    @Parameters({
            @Parameter(name="page", description = "페이지 번호", example = "1"),
            @Parameter(name="limit", description = "한페이지당 레코드 갯수", example = "20"),
            @Parameter(name="sopt", description = "검색옵션", example = "ALL"),
            @Parameter(name="skey", description = "검색키워드"),
            @Parameter(name="email", description = "이메일별로 검색"),
            @Parameter(name = "bankName", description = "은행명", examples = {
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
            @Parameter(name="payAmountMin", description = "최소금액"),
            @Parameter(name="payAmountMax", description = "최대금액"),
            @Parameter(name="sort", description = "정렬기준"),
    })
    @GetMapping("/list")
    public JSONData list(@ModelAttribute TransactionSearch search) {

        ListData<Transaction> data = new ListData<>();



        String mode = search.getMode();
        if (StringUtils.hasText(mode) && mode.equals("USER")) {
            data = infoService.getMyList(search);
        } else if (StringUtils.hasText(mode) && mode.equals("ADMIN") && memberUtil.isAdmin()) {
            data = infoService.getList(search);
        } else if (memberUtil.isAdmin()){
            data = infoService.getList(search);
        }


        return new JSONData(data);
    }
}
