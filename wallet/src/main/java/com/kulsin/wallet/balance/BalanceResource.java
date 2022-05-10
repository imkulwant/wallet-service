package com.kulsin.wallet.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceResource {
    @Autowired
    BalanceService balanceService;

    @GetMapping(value = "/balance.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public BalanceResponse getBalance(
            @RequestParam Long playerId
    ) {

        return balanceService.playerBalance(playerId);

    }

}
