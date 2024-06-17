package com.kulsin.wallet.core;

import com.kulsin.wallet.core.account.entity.Account;
import com.kulsin.wallet.core.transaction.model.TransactionRequest;

import java.util.Optional;

public class TestMockUtil {

    public static final Long PLAYER_ID = 1L;

    public static Optional<Account> mockAccount() {
        return Optional.of(new Account(PLAYER_ID, 100.0, "EUR"));
    }

    public static TransactionRequest mockDebitRequest() {
        return TransactionRequest.builder()
                .amount(30.0)
                .playerId(PLAYER_ID)
                .transactionId(2L)
                .type("DEBIT")
                .build();
    }

    public static TransactionRequest mockCreditRequest() {
        return TransactionRequest.builder()
                .amount(50.0)
                .playerId(PLAYER_ID)
                .transactionId(1L)
                .type("CREDIT")
                .build();
    }

}
