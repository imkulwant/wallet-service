package com.kulsin.wallet.core.transaction;

import com.kulsin.wallet.core.transaction.entity.Transaction;
import com.kulsin.wallet.core.transaction.model.TransactionRequest;
import com.kulsin.wallet.core.transaction.model.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse balance(Long playerId);

    TransactionResponse transact(TransactionRequest request);

    List<Transaction> playerTransactions(Long playerId);

}
