package org.anonymous.bank.services;

import lombok.RequiredArgsConstructor;
import org.anonymous.bank.entities.Bank;
import org.anonymous.bank.exceptions.BankNotFountException;
import org.anonymous.bank.repositories.BankRepository;
import org.anonymous.global.libs.Utils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class BankDeleteService {

    private final Utils utils;

    private final BankInfoService infoService;

    private final BankRepository repository;

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

        Bank item = infoService.get(seq);

        if (item == null) throw new BankNotFountException();

        item.setDeletedAt(LocalDateTime.now());

        repository.saveAndFlush(item);

        return item;
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

        List<Bank> userDeleted = new ArrayList<>();

        for (Long seq : seqs) {

            Bank item = userDelete(seq);

            if (item != null) {

                userDeleted.add(item);
            }
        }

        return userDeleted;
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

        Bank item = infoService.get(seq);

        if (item == null) throw new BankNotFountException();

        repository.delete(item);

        repository.flush();

        return item;
    }

    /**
     * 계좌 등록 목록 삭제 (관리자)
     *
     * @param seqs
     * @return
     */
    public List<Bank> deletes(List<Long> seqs) {

        List<Bank> deleted = new ArrayList<>();

        for (Long seq : seqs) {

            Bank item = delete(seq);

            if (item != null) {

                deleted.add(item);
            }
        }

        return deleted;
    }
}
