package com.kulsin.wallet.core.account;

import com.kulsin.wallet.core.account.entity.Account;
import com.kulsin.wallet.core.account.exception.AccountServiceException;
import com.kulsin.wallet.core.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public double accountBalance(Long playerId) {
        Account account = getPlayerAccount(playerId);
        return account.getBalance();
    }

    @Override
    @Transactional
    public double creditAccount(Long playerId, double amount) {
        Account account = getPlayerAccount(playerId);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        return account.getBalance();
    }

    @Override
    @Transactional
    public double debitAccount(Long playerId, double amount) {
        Account account = getPlayerAccount(playerId);

        double balance = account.getBalance();

        if (!((balance - amount) >= 0)) {
            throw new AccountServiceException("Debit declined! player has in-sufficient funds");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        return account.getBalance();
    }

    private Account getPlayerAccount(long playerId) {

        return accountRepository.findById(playerId)
                .orElseThrow(() -> new AccountServiceException("Player does not exist!"));

    }

}
