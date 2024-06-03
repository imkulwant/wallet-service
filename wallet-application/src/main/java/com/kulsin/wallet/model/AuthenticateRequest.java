package com.kulsin.wallet.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticateRequest {

    @NotNull(message = "Mandatory field playerId is missing")
    private Long playerId;

}
