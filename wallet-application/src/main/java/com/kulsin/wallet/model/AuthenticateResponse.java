package com.kulsin.wallet.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticateResponse {

    @NotNull(message = "Mandatory field playerId is missing")
    private Long playerId;

    private String sessionToken;

}
