package com.kulsin.wallet.credit;

import com.kulsin.accounting.account.AccountService;
import com.kulsin.accounting.transaction.Transaction;
import com.kulsin.accounting.transaction.TransactionService;
import com.kulsin.wallet.model.WalletRequest;
import com.kulsin.wallet.model.WalletResponse;
import com.kulsin.wallet.errorhandling.WalletException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreditService {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public CreditService(AccountService accountService,
                         TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public WalletResponse creditPlayer(WalletRequest creditRequest) {

        long playerId = creditRequest.getPlayerId();
        double amount = creditRequest.getAmount();
        String currency = creditRequest.getCurrency();
        long transactionId = creditRequest.getTransactionId();

        if(transactionService.transactionExists(transactionId)) {
            throw new WalletException(String.format("Transaction id %s is not unique!", transactionId));
        }

        Transaction transaction = new Transaction(
                transactionId,
                playerId,
                amount,
                "CREDIT",
                Instant.now().toString()
        );

        if (accountService.accountExist(playerId)) {
            double updateBalance = accountService.getBalance(playerId) + amount;
            accountService.updatePlayerBalance(playerId, updateBalance, currency);
            transactionService.saveTransaction(transaction);
        } else {
            // for simplicity creating a new account if player doesn't exist
            accountService.updatePlayerBalance(playerId, amount, currency);
            transactionService.saveTransaction(transaction);
        }

        return new WalletResponse(playerId, accountService.getBalance(playerId), transactionId);
    }

}
