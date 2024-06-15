package com.kulsin.wallet.model.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class WalletErrorResponse {

    private final HttpStatus statusCode;
    private final String message;

}
