package com.kulsin.wallet.application.controller.debit;

import com.kulsin.wallet.core.account.AccountService;
import com.kulsin.wallet.core.transaction.Transaction;
import com.kulsin.wallet.core.transaction.TransactionService;
import com.kulsin.wallet.application.model.WalletRequest;
import com.kulsin.wallet.application.model.WalletResponse;
import com.kulsin.wallet.application.errorhandling.WalletException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class DebitService {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public DebitService(AccountService accountService,
                        TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public WalletResponse debitPlayer(WalletRequest debitRequest) {

        Long playerId = debitRequest.getPlayerId();
        double debitAmount = debitRequest.getAmount();
        String currency = debitRequest.getCurrency();
        long transactionId = debitRequest.getTransactionId();

        validateDebitRequest(playerId, debitAmount, transactionId);

        double updatedBalance = accountService.getBalance(playerId) - debitAmount;

        accountService.updatePlayerBalance(playerId, updatedBalance, currency);

        transactionService.saveTransaction(new Transaction(transactionId, playerId, debitAmount,
                "DEBIT", Instant.now().toString()));

        return new WalletResponse(playerId, updatedBalance, transactionId);

    }

    private void validateDebitRequest(Long playerId, double debitAmount, long transactionId) {

        if (!accountService.accountExist(playerId)) {
            throw new WalletException("Invalid player id! player account doesn't exists");
        }

        if(transactionService.transactionExists(transactionId)) {
            throw new WalletException(String.format("Transaction id %s is not unique!", transactionId));
        }

        double balance = accountService.getBalance(playerId);
        if (!((balance - debitAmount) >= 0)) {
            throw new WalletException("Transaction declined! player has in-sufficient funds");
        }

    }

}
