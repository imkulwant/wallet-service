package com.kulsin.wallet.controller;

import com.kulsin.wallet.model.request.CreditRequest;
import com.kulsin.wallet.model.request.DebitRequest;
import com.kulsin.wallet.service.WalletService;
import com.kulsin.wallet.model.request.WalletRequest;
import com.kulsin.wallet.model.response.WalletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletResource {

    private final WalletService walletService;

    @PostMapping(value = "/authenticate", produces = APPLICATION_JSON_VALUE)
    public WalletResponse authenticate(@RequestBody @Valid WalletRequest request) {
        return walletService.authenticate(request);
    }

    @PostMapping(value = "/balance", produces = APPLICATION_JSON_VALUE)
    public WalletResponse getBalance(@RequestBody @Valid WalletRequest request) {
        return walletService.playerBalance(request);
    }

    @PostMapping(value = "/debit", produces = APPLICATION_JSON_VALUE)
    public WalletResponse debit(@RequestBody DebitRequest request) {
        return walletService.debitPlayer(request);
    }

    @PostMapping(value = "/credit", produces = APPLICATION_JSON_VALUE)
    public WalletResponse credit(@RequestBody @Valid CreditRequest request) {
        return walletService.creditPlayer(request);
    }

}
