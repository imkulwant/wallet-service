package com.kulsin.wallet.controller;

import com.kulsin.wallet.model.AuthenticateResponse;
import com.kulsin.wallet.service.WalletService;
import com.kulsin.wallet.model.TransactionHistoryResponse;
import com.kulsin.wallet.model.WalletBaseRequest;
import com.kulsin.wallet.model.WalletRequest;
import com.kulsin.wallet.model.WalletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletResource {

    private final WalletService walletService;

    @PostMapping(value = "/authenticate", produces = APPLICATION_JSON_VALUE)
    public AuthenticateResponse authenticate(@RequestBody @Valid WalletBaseRequest balanceRequest) {
        return walletService.authenticate(balanceRequest);
    }

    @PostMapping(value = "/balance", produces = APPLICATION_JSON_VALUE)
    public WalletResponse getBalance(@RequestBody @Valid WalletBaseRequest balanceRequest) {
        return walletService.playerBalance(balanceRequest.getPlayerId());
    }

    @PostMapping(value = "/debit", produces = APPLICATION_JSON_VALUE)
    public WalletResponse debit(@RequestBody WalletRequest debitRequest) {
        return walletService.debitPlayer(debitRequest);
    }

    @PostMapping(value = "/credit", produces = APPLICATION_JSON_VALUE)
    public WalletResponse credit(@RequestBody @Valid WalletRequest creditRequest) {
        return walletService.creditPlayer(creditRequest);
    }

    @GetMapping(value = "/history", produces = APPLICATION_JSON_VALUE)
    public TransactionHistoryResponse playerHistory(@RequestParam long playerId) {
        return walletService.playerHistory(playerId);
    }

}
