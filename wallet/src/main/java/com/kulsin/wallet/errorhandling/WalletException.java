package com.kulsin.wallet.errorhandling;

public class WalletException extends RuntimeException {

    public WalletException(String errorMessage) {
        super(errorMessage);
    }

}
