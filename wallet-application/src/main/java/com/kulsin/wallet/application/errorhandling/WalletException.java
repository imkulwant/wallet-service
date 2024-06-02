package com.kulsin.wallet.application.errorhandling;

public class WalletException extends RuntimeException {

    public WalletException(String errorMessage) {
        super(errorMessage);
    }

}
