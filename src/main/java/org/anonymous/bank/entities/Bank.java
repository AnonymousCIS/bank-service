package org.anonymous.bank.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.anonymous.bank.constants.BankName;
import org.anonymous.global.entities.BaseMemberEntity;

import java.util.List;

/**
 * 계좌 Entity
 */
@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
public class Bank extends BaseMemberEntity {

    @Id
    @GeneratedValue
    private Long seq;

    // 은행명
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BankName bankName;

    // 계좌 번호
    @Column(unique = true, nullable = false)
    private String accountNumber;

    // 계좌 비밀 번호
    @Column(nullable = false)
    private String password;

    // 예금주 이름
    @Column(length = 30, nullable = false)
    private String name;

    // 거래 내역
    @OneToMany
    @JsonIgnore
    @ToString.Exclude
    private List<Transaction> transactions;

    public String getBankNameStr() {
        return bankName == null ? "" : bankName.getTitle();
    }
}
