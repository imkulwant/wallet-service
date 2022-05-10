package com.kulsin.wallet.history;

import com.kulsin.account.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlayerHistory {

    private List<Transaction> transactions;

}
