package com.kulsin.accounting.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public double getBalance(long playerId) {
        Account account = accountRepository.getById(playerId);
        return account.getBalance();
    }

    @Transactional
    public Account updatePlayerBalance(long playerId, double balance, String currency) {
            return accountRepository.save(new Account(playerId, balance, currency));
    }

    public boolean accountExist(long playerId) {
        return accountRepository.existsById(playerId);
    }

}
