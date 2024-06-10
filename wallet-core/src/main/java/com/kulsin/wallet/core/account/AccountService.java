package com.kulsin.wallet.core.account;

public interface AccountService {

    double accountBalance(Long playerId);

    double creditAccount(Long playerId, double amount);

    double debitAccount(Long playerId, double amount);

}
