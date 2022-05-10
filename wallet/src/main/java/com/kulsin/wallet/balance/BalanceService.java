package com.kulsin.wallet.balance;

import com.kulsin.account.playeraccount.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    @Autowired
    AccountService accountService;

    public BalanceResponse playerBalance(Long playerId) {

        double balance = accountService.getBalance(playerId);
        return new BalanceResponse(playerId, balance);

    }

}
