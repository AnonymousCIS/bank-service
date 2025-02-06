package org.anonymous.bank.controllers;

import lombok.Data;

@Data
public class TransactionSearch extends BankSearch {

    // private String sort;

    private Long payAmountMin;

    private Long payAmountMax;
}