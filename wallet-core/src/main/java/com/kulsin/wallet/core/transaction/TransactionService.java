package com.kulsin.wallet.core.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public Transaction saveTransaction(Transaction transaction) {

        validateTransactionIsUnique(transaction.getTransactionId());

        return transactionRepository.save(transaction);

    }

    public List<Transaction> getPlayerTransactions(long playerId) {

        return transactionRepository.findAllByPlayerId(playerId);

    }

    private void validateTransactionIsUnique(long transactionId) {

        if (transactionRepository.existsById(transactionId)) {
            throw new TransactionServiceException("Transaction is not unique");
        }

    }

}
