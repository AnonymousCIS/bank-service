package org.anonymous.bank.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.anonymous.bank.entities.Bank;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class RequestTransaction {

    private Long seq;

    @NotBlank
    private Long payAmount;

    @NotBlank
    private Bank bank;
}
