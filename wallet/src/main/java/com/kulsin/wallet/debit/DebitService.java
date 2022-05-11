package com.kulsin.wallet.debit;

import com.kulsin.accounting.account.AccountService;
import com.kulsin.accounting.transaction.Transaction;
import com.kulsin.accounting.transaction.TransactionService;
import com.kulsin.wallet.common.ValidationService;
import com.kulsin.wallet.common.WalletBaseRequest;
import com.kulsin.wallet.common.WalletBaseResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class DebitService {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final ValidationService validationService;

    public DebitService(AccountService accountService,
                        TransactionService transactionService,
                        ValidationService validationService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.validationService = validationService;
    }

    public WalletBaseResponse debitPlayer(WalletBaseRequest debitRequest) {

        validationService.checkTransactionUniqueness(debitRequest.getTransactionId());

        Transaction transaction = new Transaction(
                debitRequest.getTransactionId(),
                debitRequest.getPlayerId(),
                debitRequest.getAmount(),
                "DEBIT",
                Instant.now().toString()
        );

        if (accountService.accountExist(debitRequest.getPlayerId())) {
            if( accountService.getBalance(debitRequest.getPlayerId()) >= debitRequest.getAmount()) {
                double updateBalance = accountService.getBalance(debitRequest.getPlayerId()) - debitRequest.getAmount();
                accountService.updateBalance(debitRequest.getPlayerId(), updateBalance, debitRequest.getCurrency());
                transactionService.saveTransaction(transaction);
            } else {
                throw new RuntimeException("Insufficient balance");
            }

        } else {
            throw new RuntimeException("Player account doesn't exists");
        }

        return new WalletBaseResponse(debitRequest.getPlayerId(),
                accountService.getBalance(debitRequest.getPlayerId()),
                debitRequest.getTransactionId(),
                "200OK"
        );
    }

}
