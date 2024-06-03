package com.kulsin.wallet.service;

import com.kulsin.wallet.errorhandling.WalletException;
import com.kulsin.wallet.model.AuthenticateResponse;
import com.kulsin.wallet.model.TransactionHistoryResponse;
import com.kulsin.wallet.model.WalletBaseRequest;
import com.kulsin.wallet.model.WalletRequest;
import com.kulsin.wallet.model.WalletResponse;
import com.kulsin.wallet.core.account.AccountService;
import com.kulsin.wallet.core.session.PlayerSession;
import com.kulsin.wallet.core.session.PlayerSessionService;
import com.kulsin.wallet.core.transaction.Transaction;
import com.kulsin.wallet.core.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final AccountService accountService;
    private final PlayerSessionService sessionService;
    private final TransactionService transactionService;

    public AuthenticateResponse authenticate(WalletBaseRequest request) {
        PlayerSession playerSession = sessionService.createSession(request.getPlayerId());
        return AuthenticateResponse.builder()
                .playerId(request.getPlayerId())
                .sessionToken(playerSession.getSessionToken())
                .build();
    }

    public WalletResponse playerBalance(Long playerId) {

        if (!accountService.accountExist(playerId)) {
            throw new WalletException("Invalid player id! player account doesn't exists");
        }

        double balance = accountService.getBalance(playerId);
        return new WalletResponse(playerId, balance);

    }

    public WalletResponse creditPlayer(WalletRequest creditRequest) {

        long playerId = creditRequest.getPlayerId();
        double amount = creditRequest.getAmount();
        String currency = creditRequest.getCurrency();
        long transactionId = creditRequest.getTransactionId();

        if (transactionService.transactionExists(transactionId)) {
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

        if (transactionService.transactionExists(transactionId)) {
            throw new WalletException(String.format("Transaction id %s is not unique!", transactionId));
        }

        double balance = accountService.getBalance(playerId);
        if (!((balance - debitAmount) >= 0)) {
            throw new WalletException("Transaction declined! player has in-sufficient funds");
        }

    }

    public TransactionHistoryResponse playerHistory(long playerId) {

        if (!accountService.accountExist(playerId)) {
            throw new WalletException("Invalid player id! player account doesn't exists");
        }

        return new TransactionHistoryResponse(transactionService.getTransactions(playerId));

    }

}
