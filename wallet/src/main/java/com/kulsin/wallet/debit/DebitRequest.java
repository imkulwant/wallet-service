package com.kulsin.wallet.debit;

import lombok.Data;

@Data
public class DebitRequest {

    private final Long playerId;
    private final Long amount;
    private final String transactionId;

}
