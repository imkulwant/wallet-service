package com.kulsin.account.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

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
