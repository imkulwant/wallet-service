package com.kulsin.account.playeraccount;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        try {

            return accountRepository.save(account);

        } catch (Exception e) {
            log.error("Exception occurred while creating player account", e);
            throw new RuntimeException("Exception occurred while creating player account");
        }
    }


    public Account getAccount(long playerId) {
        try {

            return accountRepository.getById(playerId);

        } catch (Exception e) {
            log.error("Exception occurred while fetching player account", e);
            throw new RuntimeException("Exception occurred while fetching player account");
        }
    }


    public double getBalance(long playerId) {
        Account account = accountRepository.getById(playerId);
        return account.getBalance();
    }

    public Account updateBalance(long playerId, double balance) {
            return accountRepository.save(new Account(playerId, balance));
    }

    public boolean accountExist(long playerId) {
        return accountRepository.existsById(playerId);
    }

}
