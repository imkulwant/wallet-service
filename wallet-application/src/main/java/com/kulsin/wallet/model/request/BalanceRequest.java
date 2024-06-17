package com.kulsin.wallet.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceRequest {

    @NotNull(message = "Mandatory field playerId is missing")
    private Long playerId;

    @NotNull(message = "Mandatory field sessionToken is missing")
    private String sessionToken;

}