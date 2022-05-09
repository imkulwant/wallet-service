package com.kulsin.wallet.debit;

import lombok.Data;

@Data
public class DebitResponse {

    private final Long playerId;
    private final Long balance;
    private final String transactionId;
    private final String transactionStatus;

}
