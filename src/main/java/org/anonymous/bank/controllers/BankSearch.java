package org.anonymous.bank.controllers;

import lombok.Data;
import org.anonymous.global.paging.CommonSearch;

import java.util.List;

@Data
public class BankSearch extends CommonSearch {

    private String sort;

    private List<String> email;

    private List<String> bankName;
}