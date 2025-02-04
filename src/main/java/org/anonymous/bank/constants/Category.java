package org.anonymous.bank.constants;


import lombok.Getter;

@Getter
public enum Category {
    CREDITLOAN(1,"신용대출"), // 신용대출
    MORTGAGELOAN(2, "담보대출"); // 담보대출


    private final int target;
    private final String title;
    Category(int target, String title) {
        this.target = target;
        this.title = title;
    }

    public String toString() {
        return title;
    }
}
