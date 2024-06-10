package com.kulsin.wallet.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class WalletRequest {

    @NotNull(message = "Mandatory field playerId is missing")
    private Long playerId;

    @NotNull(message = "Mandatory field sessionToken is missing")
    private String sessionToken;

}
