package com.kulsin.wallet.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WalletRequest extends WalletBaseRequest {

    private double amount;
    private String currency;
    @NotNull(message = "Mandatory field transactionId is missing")
    private Long transactionId;

}
