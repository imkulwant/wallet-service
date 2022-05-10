package com.kulsin.wallet;

import com.kulsin.account.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    @Autowired
    TransactionService transactionService;

    public void checkTransactionUniqueness(long transactionId) {
        if(transactionService.transactionExists(transactionId)) {
            throw new RuntimeException("Transaction already exists");
        }
    }

}
