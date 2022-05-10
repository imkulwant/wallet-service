package com.kulsin.wallet.history;

import com.kulsin.account.transaction.Transaction;
import com.kulsin.account.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HistoryResource {

    @Autowired
    TransactionService transactionService;

    @GetMapping(value = "/history.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public PlayerHistory playerHistory(
            @RequestParam long playerId
    ) {

        List<Transaction> transactions = transactionService.getTransactions(playerId);
        return new PlayerHistory(transactions);

    }

}
