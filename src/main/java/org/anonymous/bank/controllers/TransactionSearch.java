package org.anonymous.bank.controllers;

import lombok.Data;
import org.anonymous.global.paging.CommonSearch;

@Data
public class TransactionSearch extends BankSearch {

    private Long payAmountMin;
    private Long payAmountMax;
}
