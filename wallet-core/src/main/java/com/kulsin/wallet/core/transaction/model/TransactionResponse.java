package com.kulsin.wallet.core.transaction.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionResponse {

    private Long playerId;

    private double balance;

    private String currency;

    private Long transactionId;

}