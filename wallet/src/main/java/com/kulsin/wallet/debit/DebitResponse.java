package com.kulsin.wallet.debit;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DebitResponse {

    private final Long playerId;
    private final double balance;
    private final Long transactionId;
    private final String transactionStatus;

}
