package com.kulsin.accounting.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
        try {

            return transactionRepository.save(transaction);

        } catch (Exception e) {
            log.error("Exception occurred while saving player transaction", e);
            throw new RuntimeException("Exception occurred while saving player transaction", e);
        }
    }


    public List<Transaction> getTransactions(long playerId) {
        try {

            return transactionRepository.findAll().stream()
                    .filter(transaction -> playerId == transaction.getPlayerId()).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Exception occurred while fetching player transactions", e);
            throw new RuntimeException("Exception occurred while fetching player transactions", e);
        }
    }

    public boolean transactionExists(long transactionId) {
        return transactionRepository.existsById(transactionId);
    }

}
