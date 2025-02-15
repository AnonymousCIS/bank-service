package org.anonymous.bank.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.anonymous.bank.constants.CardType;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class RequestCard {

    // 카드 추천. -> 연회비, 카드종류 2개만 선택, 그리고 bankName 자동으로. 한도는 얘가 썼던 금액을 보고. 카테고리는 자동으로.
    @NotNull
    private int annualFee; // 연회비는 받자. 근데 얘 한도 걸려있음.

    @NotNull
    private CardType cardType; // 카드종류

}
