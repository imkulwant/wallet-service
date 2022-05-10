package com.kulsin.wallet.credit;

import com.kulsin.account.playeraccount.Account;
import com.kulsin.account.transaction.Transaction;
import com.kulsin.account.playeraccount.AccountService;
import com.kulsin.account.transaction.TransactionService;
import com.kulsin.wallet.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditService {

    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;

    @Autowired
    ValidationService validationService;

    public CreditResponse creditPlayer(CreditRequest creditRequest) {

        validationService.checkTransactionUniqueness(creditRequest.getTransactionId());

        Transaction transaction = new Transaction(
                creditRequest.getTransactionId(),
                creditRequest.getPlayerId(),
                creditRequest.getAmount(),
                "CREDIT"
        );

        if (accountService.accountExist(creditRequest.getPlayerId())) {
            double updateBalance = accountService.getBalance(creditRequest.getPlayerId()) + creditRequest.getAmount();
            accountService.updateBalance(creditRequest.getPlayerId(), updateBalance);
            transactionService.saveTransaction(transaction);
        } else {
            accountService.createAccount(new Account(creditRequest.getPlayerId(), creditRequest.getAmount()));
            transactionService.saveTransaction(transaction);
        }

        return new CreditResponse(creditRequest.getPlayerId(),
                accountService.getBalance(creditRequest.getPlayerId()),
                creditRequest.getTransactionId(),
                "200OK");
    }

}
