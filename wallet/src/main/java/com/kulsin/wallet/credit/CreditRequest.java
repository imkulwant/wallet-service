package com.kulsin.wallet.credit;

import lombok.Data;

@Data
public class CreditRequest {

    private final Long playerId;
    private final Long amount;
    private final String transactionId;

}
