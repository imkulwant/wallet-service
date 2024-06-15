package com.kulsin.wallet.controller;

import com.kulsin.wallet.core.transaction.entity.Transaction;
import com.kulsin.wallet.model.request.BalanceRequest;
import com.kulsin.wallet.model.request.WalletRequest;
import com.kulsin.wallet.model.response.WalletResponse;
import com.kulsin.wallet.service.WalletService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<WalletResponse> authenticate(@RequestBody @Valid BalanceRequest request) {
        return ResponseEntity.ok(walletService.authenticate(request));
    }

    @PostMapping(value = "/balance", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<WalletResponse> getBalance(@RequestBody @Valid BalanceRequest request) {
        return ResponseEntity.ok(walletService.playerBalance(request));
    }

    @PostMapping(value = "/debit", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<WalletResponse> debit(@RequestBody @Valid WalletRequest request) {
        return ResponseEntity.ok(walletService.debitPlayer(request));
    }

    @PostMapping(value = "/credit", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<WalletResponse> credit(@RequestBody @Valid WalletRequest request) {
        return ResponseEntity.ok(walletService.creditPlayer(request));
    }

    @GetMapping(value = "/history", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Transaction>> playerHistory(@RequestParam @NotNull Long playerId) {
        return ResponseEntity.ok(walletService.playerHistory(playerId));
    }

}
