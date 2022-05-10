package com.kulsin.wallet.debit;

import com.kulsin.account.playeraccount.AccountService;
import com.kulsin.account.transaction.Transaction;
import com.kulsin.account.transaction.TransactionService;
import com.kulsin.wallet.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebitService {

    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;

    @Autowired
    ValidationService validationService;


    public DebitResponse debitPlayer(DebitRequest debitRequest) {

        validationService.checkTransactionUniqueness(debitRequest.getTransactionId());

        Transaction transaction = new Transaction(
                debitRequest.getTransactionId(),
                debitRequest.getPlayerId(),
                debitRequest.getAmount(),
                "DEBIT"
        );

        if (accountService.accountExist(debitRequest.getPlayerId())) {
            if( accountService.getBalance(debitRequest.getPlayerId()) >= debitRequest.getAmount()) {
                double updateBalance = accountService.getBalance(debitRequest.getPlayerId()) - debitRequest.getAmount();
                accountService.updateBalance(debitRequest.getPlayerId(), updateBalance);
                transactionService.saveTransaction(transaction);
            } else {
                throw new RuntimeException("Insufficient balance");
            }

        } else {
            throw new RuntimeException("Player account doesn't exists");
        }

        return new DebitResponse(debitRequest.getPlayerId(),
                accountService.getBalance(debitRequest.getPlayerId()),
                debitRequest.getTransactionId(),
                "200OK");
    }

}
