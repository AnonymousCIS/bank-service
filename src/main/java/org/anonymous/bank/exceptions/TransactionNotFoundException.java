package org.anonymous.bank.exceptions;

import org.anonymous.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class TransactionNotFoundException extends CommonException {

    public TransactionNotFoundException() {

        super("NotFound.transaction", HttpStatus.NOT_FOUND);

        setErrorCode(true);
    }
}
