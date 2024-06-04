package com.kulsin.wallet.service;

import com.kulsin.wallet.core.account.Account;
import com.kulsin.wallet.errorhandling.WalletException;
import com.kulsin.wallet.model.AuthenticateResponse;
import com.kulsin.wallet.model.CreditRequest;
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

        Account account = accountService.findOrCreateAccount(request.getPlayerId());

        PlayerSession playerSession = sessionService.createSession(account.getPlayerId());

        return AuthenticateResponse.builder()
                .playerId(request.getPlayerId())
                .sessionToken(playerSession.getSessionToken())
                .build();
    }

    public WalletResponse playerBalance(Long playerId) {

        accountService.validateIfPlayerAccountExist(playerId);
        Account account = accountService.getPlayerAccount(playerId);
        return new WalletResponse(playerId, account.getBalance());

    }

    public WalletResponse creditPlayer(CreditRequest creditRequest) {

        sessionService.validateIfSessionIsActive(creditRequest.getSessionToken());

        long playerId = creditRequest.getPlayerId();
        double amount = creditRequest.getAmount();
        String currency = creditRequest.getCurrency();
        long transactionId = creditRequest.getTransactionId();

        transactionService.validateTransactionIsUnique(transactionId);

        double updatedBalance = accountService.getPlayerAccount(playerId).getBalance() + amount;

        Account account = accountService.updatePlayerAccount(new Account(playerId, updatedBalance, currency));

        transactionService.saveTransaction(new Transaction(
                transactionId,
                playerId,
                amount,
                "CREDIT",
                Instant.now().toString()
        ));

        return new WalletResponse(playerId, account.getBalance(), transactionId);
    }

    public WalletResponse debitPlayer(WalletRequest debitRequest) {

        Long playerId = debitRequest.getPlayerId();
        double debitAmount = debitRequest.getAmount();
        String currency = debitRequest.getCurrency();
        long transactionId = debitRequest.getTransactionId();

        validateDebitRequest(playerId, debitAmount, transactionId);

        double updatedBalance = accountService.getPlayerAccount(playerId).getBalance() - debitAmount;

        accountService.updatePlayerAccount(new Account(playerId, updatedBalance, currency));

        transactionService.saveTransaction(new Transaction(transactionId, playerId, debitAmount,
                "DEBIT", Instant.now().toString()));

        return new WalletResponse(playerId, updatedBalance, transactionId);

    }

    private void validateDebitRequest(Long playerId, double debitAmount, long transactionId) {

        accountService.validateIfPlayerAccountExist(playerId);

        transactionService.validateTransactionIsUnique(transactionId);

        Account account = accountService.getPlayerAccount(playerId);

        double balance = account.getBalance();

        if (!((balance - debitAmount) >= 0)) {
            throw new WalletException("Transaction declined! player has in-sufficient funds");
        }

    }

    public TransactionHistoryResponse playerHistory(long playerId) {

        accountService.validateIfPlayerAccountExist(playerId);

        return new TransactionHistoryResponse(transactionService.getPlayerTransactions(playerId));

    }

}
