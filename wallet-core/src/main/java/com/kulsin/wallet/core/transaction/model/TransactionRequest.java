package com.kulsin.wallet.core.transaction.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransactionRequest {

    private Long playerId;

    private double amount;

    private String type;

    private String currency;

    private Long transactionId;

}