package com.kulsin.wallet.credit;

import lombok.Data;

@Data
public class CreditRequest {

    private final long playerId;
    private final double amount;
    private final long transactionId;

}
