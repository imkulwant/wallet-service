package com.kulsin.wallet.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletResponse {

    private long playerId;
    private double balance;
    private long transactionId;
    private String currency;

}
