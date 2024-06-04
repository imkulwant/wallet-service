package com.kulsin.wallet.service;

import com.kulsin.wallet.core.account.Account;
import com.kulsin.wallet.errorhandling.WalletException;
import com.kulsin.wallet.model.AuthenticateResponse;
import com.kulsin.wallet.model.TransactionHistoryResponse;
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

    public AuthenticateResponse authenticate(WalletRequest request) {

        Account account = accountService.findOrCreateAccount(request.getPlayerId());

        PlayerSession playerSession = sessionService.createSession(account.getPlayerId());

        return AuthenticateResponse.builder()
                .playerId(request.getPlayerId())
                .sessionToken(playerSession.getSessionToken())
                .build();
    }

    public WalletResponse playerBalance(WalletRequest request) {

        accountService.validateIfPlayerAccountExist(request.getPlayerId());
        sessionService.validateIfSessionIsActive(request.getSessionToken());

        Account account = accountService.getPlayerAccount(request.getPlayerId());
        return WalletResponse.builder().playerId(request.getPlayerId()).balance(account.getBalance()).build();

    }

    public WalletResponse creditPlayer(WalletRequest request) {

        accountService.validateIfPlayerAccountExist(request.getPlayerId());
        sessionService.validateIfSessionIsActive(request.getSessionToken());
        transactionService.validateTransactionIsUnique(request.getTransactionId());

        long playerId = request.getPlayerId();
        double amount = request.getAmount();
        String currency = request.getCurrency();
        long transactionId = request.getTransactionId();


        double updatedBalance = accountService.getPlayerAccount(playerId).getBalance() + amount;

        Account account = accountService.updatePlayerAccount(new Account(playerId, updatedBalance, currency));

        transactionService.saveTransaction(new Transaction(
                transactionId,
                playerId,
                amount,
                "CREDIT",
                Instant.now().toString()
        ));

        return WalletResponse.builder().playerId(request.getPlayerId()).balance(account.getBalance())
                .transactionId(transactionId)
                .build();
    }

    public WalletResponse debitPlayer(WalletRequest request) {

        accountService.validateIfPlayerAccountExist(request.getPlayerId());
        sessionService.validateIfSessionIsActive(request.getSessionToken());
        transactionService.validateTransactionIsUnique(request.getTransactionId());

        Long playerId = request.getPlayerId();
        double debitAmount = request.getAmount();
        String currency = request.getCurrency();
        long transactionId = request.getTransactionId();

        Account account = accountService.getPlayerAccount(playerId);

        double balance = account.getBalance();

        if (!((balance - debitAmount) >= 0)) {
            throw new WalletException("Transaction declined! player has in-sufficient funds");
        }

        double updatedBalance = accountService.getPlayerAccount(playerId).getBalance() - debitAmount;

        accountService.updatePlayerAccount(new Account(playerId, updatedBalance, currency));

        transactionService.saveTransaction(new Transaction(transactionId, playerId, debitAmount,
                "DEBIT", Instant.now().toString()));


        return WalletResponse.builder().playerId(request.getPlayerId()).balance(account.getBalance())
                .transactionId(transactionId)
                .build();

    }

    public TransactionHistoryResponse playerHistory(long playerId) {

        accountService.validateIfPlayerAccountExist(playerId);

        return new TransactionHistoryResponse(transactionService.getPlayerTransactions(playerId));

    }

}
