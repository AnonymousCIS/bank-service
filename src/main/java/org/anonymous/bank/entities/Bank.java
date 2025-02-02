package org.anonymous.bank.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.anonymous.global.entities.BaseMemberEntity;

import java.util.List;

/**
 * 계좌 Entity
 */
@Data
@Entity
@IdClass(BankId.class)
public class Bank extends BaseMemberEntity {

    @Id
    @GeneratedValue
    private Long seq;

    // 은행명
    @Id
    private String bankName;

    // 계좌 번호
    @Id
    private String accountNumber;

    // 계좌 비밀 번호
    @Column(nullable = false)
    private String password;

    // 계좌 잔액
    @Transient
    private Long balance;

    // 예금주
    @Column(nullable = false)
    private String name;

    // 거래 내역
    @OneToMany
    @JsonIgnore
    @ToString.Exclude
    private List<Transaction> transactions;
}
