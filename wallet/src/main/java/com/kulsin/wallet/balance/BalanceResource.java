package com.kulsin.wallet.balance;

import com.kulsin.wallet.common.WalletBaseResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceResource {

    private final BalanceService balanceService;

    public BalanceResource(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping(value = "/balance.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletBaseResponse getBalance(
            @RequestParam Long playerId
    ) {

        return balanceService.playerBalance(playerId);

    }

}
