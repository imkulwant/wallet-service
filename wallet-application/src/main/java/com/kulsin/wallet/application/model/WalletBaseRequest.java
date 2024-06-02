package com.kulsin.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletBaseRequest {

    @NotNull(message = "Mandatory field playerId is missing")
    private Long playerId;

}
