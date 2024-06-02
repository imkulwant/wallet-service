package com.kulsin.wallet.application.controller.balance;

import com.kulsin.wallet.core.account.AccountService;
import com.kulsin.wallet.application.model.WalletResponse;
import com.kulsin.wallet.application.errorhandling.WalletException;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    private final AccountService accountService;

    public BalanceService(AccountService accountService) {
        this.accountService = accountService;
    }

    public WalletResponse playerBalance(Long playerId) {

        if (!accountService.accountExist(playerId)) {
            throw new WalletException("Invalid player id! player account doesn't exists");
        }

        double balance = accountService.getBalance(playerId);
        return new WalletResponse(playerId, balance);

    }

}
