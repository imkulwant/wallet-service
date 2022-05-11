package com.kulsin.wallet.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WalletBaseRequest {

    @NotNull(message = "Mandatory field playerId is missing")
    private Long playerId;

}
