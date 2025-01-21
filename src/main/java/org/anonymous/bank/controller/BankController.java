package org.anonymous.bank.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.rests.JSONData;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BankController {
    /*
    * - POST /save : 계좌 생성, 수정 처리
- GET /view/{accountNumber} : 계좌 단일 조회 - 계좌번호
- GET /list : 계좌 목록 조회
- PATCH /deletes : 계좌 단일 | 목록 삭제
    * */

    /**
     * 계좌 생성, 수정 처리
     * @return
     */
    @PostMapping("/save")
    public JSONData save() {


        return null;
    }

    /**
     * 계좌 단일 조회 - 계좌번호
     * @param accountNumber
     * @return
     */
    @GetMapping("/view/{accountNumber}")
    public JSONData view(@PathVariable("accountNumber") String accountNumber) {


        return null;
    }

    /**
     * 계좌 목록 조회
     * @return
     */
    @GetMapping("/list")
    public JSONData list() {

        return null;
    }

    /**
     * 계좌 단일 | 목록 삭제
     *
     * @return
     */
    @PatchMapping("/deletes")
    public JSONData deletes() {


        return null;
    }
}
