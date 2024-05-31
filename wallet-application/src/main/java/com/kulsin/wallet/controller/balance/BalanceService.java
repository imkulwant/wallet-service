package com.kulsin.wallet.controller.balance;

import com.kulsin.accounting.account.AccountService;
import com.kulsin.wallet.model.WalletResponse;
import com.kulsin.wallet.errorhandling.WalletException;
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
