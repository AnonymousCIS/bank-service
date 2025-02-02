package org.anonymous.bank.services;

import lombok.RequiredArgsConstructor;
import org.anonymous.bank.entities.Bank;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class BankDeleteService {

    /**
     * 계좌 등록 단일 삭제 (일반 사용자)
     *
     * DB 에서 삭제 X
     * 현재 시간으로 DeletedAt 할당
     *
     * Base Method
     *
     * @param seq
     * @return
     */
    public Bank userDelete(Long seq) {

        return null;
    }

    /**
     * 계좌 등록 목록 삭제 (일반 사용자)
     *
     * DB 에서 삭제 X
     * 현재 시간으로 DeletedAt 할당
     *
     * @param seqs
     * @return
     */
    public List<Bank> userDeletes(List<Long> seqs) {

        return null;
    }

    /**
     * 계좌 등록 단일 삭제 (관리자)
     *
     * Base Method
     *
     * @param seq
     * @return
     */
    public Bank delete(Long seq) {

        return null;
    }

    /**
     * 계좌 등록 목록 삭제 (관리자)
     *
     * @param seqs
     * @return
     */
    public List<Bank> deletes(List<Long> seqs) {

        return null;
    }
}
