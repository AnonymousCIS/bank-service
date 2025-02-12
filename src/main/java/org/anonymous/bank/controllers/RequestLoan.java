package org.anonymous.bank.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.anonymous.bank.constants.Category;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class RequestLoan {


    // 5. 대출 추천. -> 한도는 얘가 썼던 금액을 보고, bankName 자동, category 선택, 이자율 랜덤, 상환날짜 랜덤.

    @NotBlank
    private Category category;
}
