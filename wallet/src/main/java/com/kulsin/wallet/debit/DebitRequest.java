package com.kulsin.wallet.debit;

import lombok.Data;

@Data
public class DebitRequest {

    private final Long playerId;
    private final double amount;
    private final Long transactionId;

}
