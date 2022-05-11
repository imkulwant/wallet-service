package com.kulsin.wallet.balance;

import com.kulsin.accounting.account.AccountService;
import com.kulsin.wallet.common.WalletBaseResponse;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    private final AccountService accountService;

    public BalanceService(AccountService accountService) {
        this.accountService = accountService;
    }

    public WalletBaseResponse playerBalance(Long playerId) {

        double balance = accountService.getBalance(playerId);
        return new WalletBaseResponse(playerId,
                balance,
                345678087,
                "200OK"
                );

    }

}
