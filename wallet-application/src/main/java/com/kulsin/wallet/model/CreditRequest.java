package com.kulsin.wallet.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreditRequest extends WalletBaseRequest {

    private double amount;

    private String currency;

    @NotNull(message = "Mandatory field sessionToken is missing")
    private String sessionToken;

    @NotNull(message = "Mandatory field transactionId is missing")
    private Long transactionId;

}
