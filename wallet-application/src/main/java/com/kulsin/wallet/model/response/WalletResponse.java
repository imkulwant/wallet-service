package com.kulsin.wallet.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletResponse {

    private long playerId;

    private double balance;

    private String currency;

    private Long transactionId;

}
