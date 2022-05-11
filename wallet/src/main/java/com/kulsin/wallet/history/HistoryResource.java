package com.kulsin.wallet.history;

import com.kulsin.accounting.account.AccountService;
import com.kulsin.accounting.transaction.Transaction;
import com.kulsin.accounting.transaction.TransactionService;
import com.kulsin.wallet.errorhandling.WalletException;
import com.kulsin.wallet.model.TransactionHistoryResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HistoryResource {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public HistoryResource(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping(value = "/history.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionHistoryResponse playerHistory(@RequestParam long playerId) {

        if (!accountService.accountExist(playerId)) {
            throw new WalletException("Invalid player id! player account doesn't exists");
        }

        return new TransactionHistoryResponse(transactionService.getTransactions(playerId));

    }

}
