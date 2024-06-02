package com.kulsin.wallet.application.model;

import com.kulsin.wallet.core.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryResponse {
    List<Transaction> transactions;
}
