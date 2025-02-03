package org.anonymous.bank.validators;

import lombok.RequiredArgsConstructor;
import org.anonymous.bank.controllers.RequestBank;
import org.anonymous.global.exceptions.UnAuthorizedException;
import org.anonymous.global.libs.Utils;
import org.anonymous.member.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Lazy
@Component
@RequiredArgsConstructor
public class BankValidator implements Validator {

    private final Utils utils;

    private final MemberUtil memberUtil;

    @Override
    public boolean supports(Class<?> clazz) {

        return clazz.isAssignableFrom(RequestBank.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        RequestBank form = (RequestBank) target;

        if (!memberUtil.isLogin()) throw new UnAuthorizedException();

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bankName", "NotBlank");

        String bankName = form.getBankName();

        // 은행명에 숫자가 들어가거나 특수문자가 들어갈 경우
        if (bankName.matches(".*\\d.*") || bankName.matches(".*[^0-9a-zA-Zㄱ-ㅎ가-힣].*")) {

            errors.rejectValue("bankName", "typeMismatch");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotBlank");

        String name = form.getName();

        // 예금주에 숫자가 들어가거나 특수문자가 들어갈 경우
        if (name.matches(".*\\d.*") || name.matches(".*[^0-9a-zA-Zㄱ-ㅎ가-힣].*")) {

            errors.rejectValue("name", "typeMismatch");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "accountNumber", "NotBlank");

        String accountNumber = form.getAccountNumber();

        // 계좌번호에 숫자가 아닌 문자가 들어갈 경우
        if (!accountNumber.matches(".*\\d.*")) {

            errors.rejectValue("accountNumber", "typeMismatch");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotBlank");

        String password = form.getPassword();

        // 비밀번호에 숫자가 아닌 문자가 들어갈 경우
        if (!password.matches(".*\\d.*")) {

            errors.rejectValue("password", "typeMismatch");
        }
    }
}
