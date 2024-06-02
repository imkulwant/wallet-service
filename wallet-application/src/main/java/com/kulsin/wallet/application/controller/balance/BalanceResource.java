package com.kulsin.wallet.application.controller.balance;

import com.kulsin.wallet.application.model.WalletBaseRequest;
import com.kulsin.wallet.application.model.WalletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class BalanceResource {

    private final BalanceService balanceService;

    public BalanceResource(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @PostMapping(value = "/v1/wallet/balance.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletResponse getBalance(
            @RequestBody @Valid WalletBaseRequest balanceRequest
    ) {

        return balanceService.playerBalance(balanceRequest.getPlayerId());

    }

}
