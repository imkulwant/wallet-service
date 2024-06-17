package com.kulsin.wallet.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletErrorResponse {

    private final String statusCode;
    private final String message;

}
