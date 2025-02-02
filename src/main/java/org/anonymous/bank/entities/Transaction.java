package org.anonymous.bank.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.anonymous.global.entities.BaseMemberEntity;

/**
 * 거래 내역
 */
@Data
@Entity
public class Transaction extends BaseMemberEntity {

    @Id
    @GeneratedValue
    private Long seq;

    // 지불 금액
    @Column(nullable = false)
    private Long payAmount;

    // 지불 계좌
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seq")
    private Bank bank;
}
