package com.kulsin.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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
