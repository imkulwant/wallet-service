package com.kulsin.wallet.controller;

import com.kulsin.wallet.core.transaction.entity.Transaction;
import com.kulsin.wallet.model.request.BalanceRequest;
import com.kulsin.wallet.model.request.WalletRequest;
import com.kulsin.wallet.model.response.WalletResponse;
import com.kulsin.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletResource {

    private final WalletService walletService;

    @PostMapping(value = "/authenticate", produces = APPLICATION_JSON_VALUE)
    public WalletResponse authenticate(@RequestBody @Valid BalanceRequest request) {
        return walletService.authenticate(request);
    }

    @PostMapping(value = "/balance", produces = APPLICATION_JSON_VALUE)
    public WalletResponse getBalance(@RequestBody @Valid BalanceRequest request) {
        return walletService.playerBalance(request);
    }

    @PostMapping(value = "/debit", produces = APPLICATION_JSON_VALUE)
    public WalletResponse debit(@RequestBody WalletRequest request) {
        return walletService.debitPlayer(request);
    }

    @PostMapping(value = "/credit", produces = APPLICATION_JSON_VALUE)
    public WalletResponse credit(@RequestBody @Valid WalletRequest request) {
        return walletService.creditPlayer(request);
    }

    @GetMapping(value = "/history", produces = APPLICATION_JSON_VALUE)
    public List<Transaction> playerHistory(@RequestParam long playerId) {
        return walletService.playerHistory(playerId);
    }

}
