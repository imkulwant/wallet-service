package com.kulsin.wallet.common;

import lombok.Data;

@Data
public class WalletBaseResponse {

    private final long playerId;
    private final double balance;
    private final long transactionId;
    private final String transactionStatus;

}
