package com.kulsin.wallet.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitRequest extends WalletRequest {

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private double amount;

    private String currency;

    @NotNull(message = "Transaction ID cannot be null")
    private Long transactionId;

    @Builder
    public DebitRequest(Long playerId, String sessionToken, double amount, String currency, Long transactionId) {
        super(playerId, sessionToken);
        this.amount = amount;
        this.currency = currency;
        this.transactionId = transactionId;
    }

}
