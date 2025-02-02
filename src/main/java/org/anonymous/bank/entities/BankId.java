package org.anonymous.bank.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BankId {

    private Long seq;

    private String bankName;

    private String accountNumber;
}