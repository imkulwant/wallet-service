package com.kulsin.wallet.balance;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceResource {

    BalanceService balanceService;

    BalanceResource(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping(value = "/balance.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public BalanceResponse getBalance(
            @RequestParam Long playerId,
            @RequestParam String token
    ) {

        // validate token
        return balanceService.playerBalance(playerId);

    }

}
