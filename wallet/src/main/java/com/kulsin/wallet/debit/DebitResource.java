package com.kulsin.wallet.debit;

import com.kulsin.wallet.common.WalletBaseRequest;
import com.kulsin.wallet.common.WalletBaseResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebitResource {

    private final DebitService debitService;

    public DebitResource(DebitService debitService) {
        this.debitService = debitService;
    }

    @PostMapping(value = "/debit.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletBaseResponse debit(
            @RequestBody WalletBaseRequest debitRequest
    ) {

        return debitService.debitPlayer(debitRequest);

    }

}
