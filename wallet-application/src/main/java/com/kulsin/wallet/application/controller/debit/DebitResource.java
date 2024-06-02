package com.kulsin.wallet.controller.debit;

import com.kulsin.wallet.model.WalletRequest;
import com.kulsin.wallet.model.WalletResponse;
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

    @PostMapping(value = "/v1/wallet/debit.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletResponse debit(
            @RequestBody WalletRequest debitRequest
    ) {

        return debitService.debitPlayer(debitRequest);

    }

}
