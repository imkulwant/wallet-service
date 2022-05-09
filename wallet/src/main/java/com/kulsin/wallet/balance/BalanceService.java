package com.kulsin.wallet.balance;

import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    public BalanceResponse playerBalance(Long playerId) {
        return new BalanceResponse(playerId, 54L);
    }

}
