package com.kulsin.accounting.account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

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
