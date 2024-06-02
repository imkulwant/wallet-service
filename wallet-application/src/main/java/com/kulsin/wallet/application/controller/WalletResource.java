package com.kulsin.wallet.application.controller;

import com.kulsin.wallet.application.controller.balance.BalanceService;
import com.kulsin.wallet.application.model.WalletBaseRequest;
import com.kulsin.wallet.application.model.WalletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletResource {

    private final BalanceService balanceService;

    @PostMapping(value = "/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletResponse getBalance(
            @RequestBody @Valid WalletBaseRequest balanceRequest
    ) {
        return balanceService.playerBalance(balanceRequest.getPlayerId());
    }

}
