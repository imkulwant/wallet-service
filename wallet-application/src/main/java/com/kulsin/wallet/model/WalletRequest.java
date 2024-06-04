package com.kulsin.wallet.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WalletRequest extends WalletBaseRequest {

    private double amount;
    private String currency;
    @NotNull(message = "Mandatory field transactionId is missing")
    private Long transactionId;

}
