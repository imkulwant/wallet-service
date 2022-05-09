package com.kulsin.wallet.credit;

import lombok.Data;

@Data
public class CreditResponse {

    private final Long playerId;
    private final Long balance;
    private final String transactionId;
    private final String transactionStatus;

}
