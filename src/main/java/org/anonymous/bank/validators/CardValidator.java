package org.anonymous.bank.validators;

import org.anonymous.bank.controllers.RequestCard;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Lazy
@Component
public class CardValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestCard.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (errors.hasErrors()) {
            return;
        }

        RequestCard card = (RequestCard) target;

        int annualFee = card.getAnnualFee();

        if(annualFee < 1000 || annualFee > 30000) {
            errors.rejectValue("annualFee", "annualFee.invalid");
        }
    }
}