package org.anonymous.bank.validators;

import lombok.RequiredArgsConstructor;
import org.anonymous.bank.controllers.RequestBank;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Lazy
@Component
@RequiredArgsConstructor
public class BankValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {

        return clazz.isAssignableFrom(RequestBank.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
