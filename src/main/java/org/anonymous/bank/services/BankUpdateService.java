package org.anonymous.bank.services;

import lombok.RequiredArgsConstructor;
import org.anonymous.bank.controllers.RequestBank;
import org.anonymous.bank.entities.Bank;
import org.anonymous.bank.exceptions.BankNotFountException;
import org.anonymous.bank.repositories.BankRepository;
import org.anonymous.global.exceptions.UnAuthorizedException;
import org.anonymous.member.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Lazy
@Service
@Transactional
@RequiredArgsConstructor
public class BankUpdateService {

    private final PasswordEncoder passwordEncoder;

    private final BankRepository repository;

    private final MemberUtil memberUtil;

    /**
     * 계좌 등록 | 수정
     *
     * @param form
     * @return
     */
    public Bank process(RequestBank form) {

        Long seq = Objects.requireNonNullElse(form.getSeq(), 0L);

        String mode = Objects.requireNonNullElse(form.getMode(), "add");

        Bank data = null;

        // 관리자만 계좌 수정 가능
        if (mode.equals("edit") && !memberUtil.isAdmin()) {

            throw new UnAuthorizedException();
        }

        if (mode.equals("edit") && memberUtil.isAdmin()) { // 계좌 수정

            data = repository.findById(seq).orElseThrow(BankNotFountException::new);

        } else { // 계좌 등록

            /**
             * 신규 계좌 등록시 최초 한번만 기록되는 데이터
             * - 은행명
             * - 계좌 번호
             */

            data = new Bank();

            data.setBankName(form.getBankName());
            data.setAccountNumber(form.getAccountNumber());
        }

        String password = form.getPassword();

        /* 신규 계좌 등록 & 수정 공통 반영 사항 S */

        // 비밀번호 해시화
        if (StringUtils.hasText(password)) {

            data.setPassword(passwordEncoder.encode(password));
        }

        data.setName(form.getName());

        repository.saveAndFlush(data);

        return data;
        /* 신규 계좌 등록 & 수정 공통 반영 사항 E */
    }
}