package com.kulsin.wallet.core.transaction;

import com.kulsin.wallet.core.account.AccountService;
import com.kulsin.wallet.core.transaction.entity.Transaction;
import com.kulsin.wallet.core.transaction.exception.TransactionServiceException;
import com.kulsin.wallet.core.transaction.model.TransactionRequest;
import com.kulsin.wallet.core.transaction.model.TransactionResponse;
import com.kulsin.wallet.core.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;

    @Override
    public TransactionResponse balance(Long playerId) {

        double balance = accountService.accountBalance(playerId);

        return TransactionResponse.builder()
                .playerId(playerId)
                .balance(balance)
                .build();

    }

    @Override
    @Transactional
    public TransactionResponse transact(TransactionRequest request) {

        if (transactionRepository.existsById(request.getTransactionId())) {
            throw new TransactionServiceException("Transaction is not unique");
        }

        double balance = 0;

        if ("CREDIT".equalsIgnoreCase(request.getType())) {

            balance = accountService.creditAccount(request.getPlayerId(), request.getAmount());

        } else if ("DEBIT".equalsIgnoreCase(request.getType())) {

            balance = accountService.debitAccount(request.getPlayerId(), request.getAmount());

        }

        transactionRepository.save(
                Transaction.builder()
                        .transactionId(request.getTransactionId())
                        .playerId(request.getPlayerId())
                        .amount(request.getAmount())
                        .transactionType(request.getType())
                        .timestamp(Instant.now().toString())
                        .build());

        return TransactionResponse.builder()
                .playerId(request.getPlayerId())
                .balance(balance)
                .transactionId(request.getTransactionId())
                .build();

    }

    @Override
    public List<Transaction> playerTransactions(Long playerId) {

        return transactionRepository.findAllByPlayerId(playerId);

    }

}
