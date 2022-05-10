package com.kulsin.wallet.balance;

import lombok.Data;

@Data
public class BalanceResponse {

    private final Long playerId;
    private final double balance;

}
