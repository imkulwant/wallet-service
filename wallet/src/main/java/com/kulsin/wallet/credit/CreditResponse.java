package com.kulsin.wallet.credit;

import lombok.Data;

@Data
public class CreditResponse {

    private final Long playerId;
    private final double balance;
    private final long transactionId;
    private final String transactionStatus;

}
