package org.anonymous.bank.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.anonymous.global.entities.BaseMemberEntity;

/**
 * 거래 내역
 */
@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
public class Transaction extends BaseMemberEntity {

    @Id
    @GeneratedValue
    private Long seq;

    // 지불 금액
    @Column(length = 15, nullable = false)
    private Long payAmount;

    // 지불 계좌
    @ManyToOne(fetch = FetchType.LAZY)
    private Bank bank;
}
