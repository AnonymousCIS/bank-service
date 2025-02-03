package org.anonymous.bank.exceptions;

import org.anonymous.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class BankNotFountException extends CommonException {

    public BankNotFountException() {

        super("NotFound.bank", HttpStatus.NOT_FOUND);

        setErrorCode(true);
    }
}
