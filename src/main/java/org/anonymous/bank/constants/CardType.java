package org.anonymous.bank.constants;

import lombok.Getter;

@Getter
public enum CardType {

    PersonalCheck(1, "개인체크"), // 개인체크
    PersonalCredit(2, "개인신용"), // 개인신용
    CorporateCheck(3, "법인체크"), // 법인체크
    CorporateCredit(4, "법인신용"); // 법인신용


    private final int target;
    private final String title;
    CardType(int target, String title) {
        this.target = target;
        this.title = title;
    }

    public String toString() {
        return title;
    }
}
