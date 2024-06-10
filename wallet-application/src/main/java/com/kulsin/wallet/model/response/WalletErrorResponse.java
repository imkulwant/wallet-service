package com.kulsin.wallet.model.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public class WalletErrorResponse {

    private final HttpStatus statusCode;
    private final String message;

}
