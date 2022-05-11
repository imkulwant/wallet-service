package com.kulsin.wallet.credit;

import com.kulsin.accounting.account.AccountService;
import com.kulsin.accounting.transaction.Transaction;
import com.kulsin.accounting.transaction.TransactionService;
import com.kulsin.wallet.common.ValidationService;
import com.kulsin.wallet.common.WalletBaseRequest;
import com.kulsin.wallet.common.WalletBaseResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreditService {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final ValidationService validationService;

    public CreditService(AccountService accountService,
                         TransactionService transactionService,
                         ValidationService validationService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.validationService = validationService;
    }

    public WalletBaseResponse creditPlayer(WalletBaseRequest creditRequest) {

        long playerId = creditRequest.getPlayerId();
        double amount = creditRequest.getAmount();
        String currency = creditRequest.getCurrency();
        long transactionId = creditRequest.getTransactionId();

        validationService.checkTransactionUniqueness(creditRequest.getTransactionId());

        Transaction transaction = new Transaction(
                transactionId,
                playerId,
                amount,
                "CREDIT",
                Instant.now().toString()
        );

        if (accountService.accountExist(playerId)) {
            double updateBalance = accountService.getBalance(playerId) + amount;
            accountService.updateBalance(playerId, updateBalance, currency);
            transactionService.saveTransaction(transaction);
        } else {
            accountService.updateBalance(playerId, amount, currency);
            transactionService.saveTransaction(transaction);
        }

        return new WalletBaseResponse(playerId,
                accountService.getBalance(playerId),
                creditRequest.getTransactionId(),
                "200OK"
        );
    }

}
