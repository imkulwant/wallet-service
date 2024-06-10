package com.kulsin.wallet.core.account.exception;

public class AccountServiceException extends RuntimeException {

    public AccountServiceException(String errorMessage) {
        super(errorMessage);
    }

    public AccountServiceException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
