package org.anonymous.bank.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.anonymous.bank.entities.Bank;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class RequestTransaction {

    private Long seq;

    private long payAmount;

    @NotNull
    private Bank bank;
}
