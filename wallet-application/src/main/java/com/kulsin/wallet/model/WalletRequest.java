package com.kulsin.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequest {

    @NotNull(message = "Mandatory field playerId is missing")
    private Long playerId;

    @NotNull(message = "Mandatory field playerId is missing")
    private double amount;

    private String currency;

    @NotNull(message = "Mandatory field sessionToken is missing")
    private String sessionToken;

    @NotNull(message = "Mandatory field transactionId is missing")
    private Long transactionId;

}
