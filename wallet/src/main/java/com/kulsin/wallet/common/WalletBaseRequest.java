package com.kulsin.wallet.common;

import lombok.Data;

@Data
public class WalletBaseRequest {

    private final long playerId;
    private final double amount;
    private final String currency;
    private final long transactionId;

}
