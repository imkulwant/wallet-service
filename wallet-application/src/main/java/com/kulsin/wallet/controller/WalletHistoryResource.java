package com.kulsin.wallet.controller;

import com.kulsin.wallet.core.transaction.entity.Transaction;
import com.kulsin.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/wallet/history")
@RequiredArgsConstructor
public class WalletHistoryResource {

    private final WalletService walletService;

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public List<Transaction> playerHistory(@RequestParam long playerId) {
        return walletService.playerHistory(playerId);
    }

}
