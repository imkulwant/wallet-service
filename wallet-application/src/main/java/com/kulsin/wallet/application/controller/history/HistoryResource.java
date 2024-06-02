package com.kulsin.wallet.controller.history;

import com.kulsin.wallet.core.account.AccountService;
import com.kulsin.wallet.core.transaction.TransactionService;
import com.kulsin.wallet.errorhandling.WalletException;
import com.kulsin.wallet.model.TransactionHistoryResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class HistoryResource {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public HistoryResource(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping(value = "/v1/wallet/history.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionHistoryResponse playerHistory(@RequestParam long playerId) {

        if (!accountService.accountExist(playerId)) {
            throw new WalletException("Invalid player id! player account doesn't exists");
        }

        return new TransactionHistoryResponse(transactionService.getTransactions(playerId));

    }

}
