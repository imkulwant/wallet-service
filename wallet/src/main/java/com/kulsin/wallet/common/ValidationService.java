package com.kulsin.wallet.common;

import com.kulsin.accounting.transaction.TransactionService;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    private final TransactionService transactionService;

    public ValidationService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void checkTransactionUniqueness(long transactionId) {
        if(transactionService.transactionExists(transactionId)) {
            throw new RuntimeException("Transaction already exists");
        }
    }

}
