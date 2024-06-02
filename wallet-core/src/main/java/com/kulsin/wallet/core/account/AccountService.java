package com.kulsin.wallet.core.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public double getBalance(long playerId) {
        try {
            Account account = accountRepository.getById(playerId);
            return account.getBalance();
        } catch (Exception e) {
            throw new AccountServiceException("Exception occurred while fetching player balance", e);
        }
    }

    @Transactional
    public Account updatePlayerBalance(long playerId, double balance, String currency) {
        try {
            return accountRepository.save(new Account(playerId, balance, currency));

        } catch (Exception e) {
            throw new AccountServiceException("Exception occurred while updating player balance", e);
        }
    }

    public boolean accountExist(long playerId) {
        try {
            return accountRepository.existsById(playerId);
        } catch (Exception e) {
            throw new AccountServiceException("Exception occurred while checking player account", e);
        }
    }

}
