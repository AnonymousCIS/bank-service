package org.anonymous.bank.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.anonymous.bank.constants.BankName;

/**
 * 계좌 등록 양식
 *
 */
@Data
public class RequestBank {
    
    private Long seq;

    // 등록 || 수정
    private String mode;
    
    // 은행명
    @NotNull
    private BankName bankName;
    
    // 계좌 번호
    @NotBlank
    private String accountNumber;
    
    // 예금주
    @NotBlank
    @Size(max = 30)
    private String name;
    
    // 계좌 비밀 번호
    @NotBlank
    @Size(min = 4, max = 4)
    private String password;
}