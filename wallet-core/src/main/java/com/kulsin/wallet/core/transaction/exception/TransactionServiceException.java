package com.kulsin.wallet.core.transaction.exception;

public class TransactionServiceException extends RuntimeException {

    public TransactionServiceException(String errorMessage) {
        super(errorMessage);
    }

    public TransactionServiceException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
