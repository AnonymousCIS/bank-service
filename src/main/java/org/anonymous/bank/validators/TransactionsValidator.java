package org.anonymous.bank.validators;

import lombok.RequiredArgsConstructor;
import org.anonymous.bank.controllers.RequestTransaction;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Lazy
@Component
@RequiredArgsConstructor
public class TransactionsValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {

        return clazz.isAssignableFrom(RequestTransaction.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
