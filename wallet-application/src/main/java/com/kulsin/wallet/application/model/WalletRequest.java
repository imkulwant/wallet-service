package com.kulsin.wallet.application.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class WalletRequest extends WalletBaseRequest {

    private double amount;
    private String currency;
    @NotNull(message = "Mandatory field transactionId is missing")
    private Long transactionId;

    public WalletRequest(Long playerId, double amount, String currency, Long transactionId) {
        super(playerId);
        this.amount = amount;
        this.currency = currency;
        this.transactionId = transactionId;
    }
}
