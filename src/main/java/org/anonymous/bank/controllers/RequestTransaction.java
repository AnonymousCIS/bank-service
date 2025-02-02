package org.anonymous.bank.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.anonymous.bank.entities.Bank;

@Data
public class RequestTransaction {

    private Long seq;

    @NotBlank
    private Long payAmount;

    @NotBlank
    private Bank bank;
}
