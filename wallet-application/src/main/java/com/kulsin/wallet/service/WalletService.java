package com.kulsin.wallet.service;

import com.kulsin.wallet.core.session.PlayerSessionService;
import com.kulsin.wallet.core.transaction.entity.Transaction;
import com.kulsin.wallet.core.transaction.model.TransactionRequest;
import com.kulsin.wallet.core.transaction.model.TransactionResponse;
import com.kulsin.wallet.core.transaction.TransactionService;
import com.kulsin.wallet.model.request.CreditRequest;
import com.kulsin.wallet.model.request.DebitRequest;
import com.kulsin.wallet.model.request.WalletRequest;
import com.kulsin.wallet.model.response.WalletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final ConversionService conversionService;
    private final PlayerSessionService sessionService;
    private final TransactionService transactionService;

    public WalletResponse authenticate(WalletRequest request) {

        sessionService.validateIfSessionIsActive(request.getSessionToken());

        return playerBalance(request);
    }

    public WalletResponse playerBalance(WalletRequest request) {

        sessionService.validateIfSessionIsActive(request.getSessionToken());

        TransactionResponse response = transactionService.balance(request.getPlayerId());

        return WalletResponse.builder()
                .playerId(request.getPlayerId())
                .balance(response.getBalance())
                .build();

    }

    public WalletResponse creditPlayer(CreditRequest request) {

        sessionService.validateIfSessionIsActive(request.getSessionToken());

        TransactionRequest transaction = conversionService.convert(request, TransactionRequest.class);

        TransactionResponse response = transactionService.transact(transaction);

        return conversionService.convert(response, WalletResponse.class);
    }

    public WalletResponse debitPlayer(DebitRequest request) {

        sessionService.validateIfSessionIsActive(request.getSessionToken());

        TransactionRequest transaction = conversionService.convert(request, TransactionRequest.class);

        TransactionResponse response = transactionService.transact(transaction);

        return conversionService.convert(response, WalletResponse.class);

    }

    public List<Transaction> playerHistory(long playerId) {

       return transactionService.playerTransactions(playerId);

    }

}
