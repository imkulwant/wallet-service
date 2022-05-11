package com.kulsin.wallet.history;

import com.kulsin.accounting.transaction.Transaction;
import com.kulsin.accounting.transaction.TransactionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HistoryResource {

    private final TransactionService transactionService;

    public HistoryResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(value = "/history.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Transaction> playerHistory(
            @RequestParam long playerId
    ) {

        return transactionService.getTransactions(playerId);

    }

}
