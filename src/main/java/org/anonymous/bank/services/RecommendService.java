package org.anonymous.bank.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.anonymous.bank.constants.CardType;
import org.anonymous.bank.controllers.RequestCard;
import org.anonymous.bank.controllers.RequestLoan;
import org.anonymous.bank.entities.Transaction;
import org.anonymous.bank.services.transaction.TransactionInfoService;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.rests.JSONData;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Random;

@Lazy
@Service
@RequiredArgsConstructor
public class RecommendService {


    private final Utils utils;
    private final ObjectMapper om;
    private final RestTemplate restTemplate;
    private final TransactionInfoService transactionInfoService;

    public String recommendCard(RequestCard card) {

        Random random = new Random();

        // 4. 카드 추천. -> 연회비, 카드종류 2개만 선택, 그리고 bankName 자동으로. 한도는 얘가 썼던 금액을 보고. 카테고리는 자동으로.

        // 1. 연회비

        int annualFeeTarget = card.getAnnualFee() / 1000; // 첫번째 연회비 타겟

        // 2. 카드종류

        CardType cardType = card.getCardType();
        int cardTypeTarget = cardType.getTarget(); // 두번째 카드종류

        // 3. 카드한도

        Long checkMoneyTarget = addInfo();

        // 4. 은행종류

        int bankNameTarget = random.nextInt(0, 23);

        // 5. category

        int categoryTarget = random.nextInt(1, 5);

        // region API 전달

        String data = String.format("%s_%s_%s_%s_%s", annualFeeTarget, cardTypeTarget, checkMoneyTarget, bankNameTarget, categoryTarget);

        ResponseEntity<JSONData> item = addInfo("card-service", data);

        try {
            return om.writeValueAsString(item.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        // endregion
    }


    public String recommendLoan(RequestLoan loan) {

        Random random = new Random();

        // 5. 대출 추천. -> 한도는 얘가 썼던 금액을 보고, bankName 자동, category 선택, 이자율 랜덤, 상환날짜 랜덤.

        // 1. 특성1 대출한도

        Long checkMoneyTarget = addInfo() * 10;

        // 2. 특성2 은행종류

        int bankNameTarget = random.nextInt(0, 23);

        // 3. 특성3 대출종류

        int category = loan.getCategory().getTarget();

        // 4. 대출 금리

        int interestRate = random.nextInt(1, 11);

        // 5. 대출 상환년도

        int dateYear = random.nextInt(1, 51);

        // region API 전달

        String data = String.format("%s_%s_%s_%s_%s", checkMoneyTarget, bankNameTarget, category, interestRate, dateYear);
        ResponseEntity<JSONData> item = addInfo("loan-service", data);

        try {
            return om.writeValueAsString(item.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        // endregion
    }

    private Long addInfo() {
        List<Transaction> transactions = transactionInfoService.getByDate();

        Long checkMoney = 0L;

        for (Transaction transaction : transactions) {
            checkMoney += transaction.getPayAmount();
        }

        return checkMoney / 1000000L; // 세번째 Limit
    }

    private ResponseEntity<JSONData> addInfo(String url, String data) {
        String token = utils.getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.hasText(token)) {
            headers.setBearerAuth(token);
        }

        HttpEntity<Void> request = new HttpEntity<>(headers);

        String apiUrl = utils.serviceUrl(url, "/predict?data=" + data);
        return restTemplate.exchange(URI.create(apiUrl), HttpMethod.GET, request, JSONData.class);
    }
}
















