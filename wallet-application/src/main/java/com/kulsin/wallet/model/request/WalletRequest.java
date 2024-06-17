package com.kulsin.wallet.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequest {

    @NotNull(message = "Mandatory field playerId is missing")
    private Long playerId;

    @NotNull(message = "Mandatory field sessionToken is missing")
    private String sessionToken;

    @NotNull(message = "Type can not be null")
    private String type;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private double amount;

    private String currency;

    @NotNull(message = "Transaction ID cannot be null")
    private Long transactionId;

}
