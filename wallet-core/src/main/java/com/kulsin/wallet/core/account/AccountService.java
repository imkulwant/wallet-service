package com.kulsin.wallet.core.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account findOrCreateAccount(long playerId) {

        if (accountRepository.existsById(playerId)) {
            return getPlayerAccount(playerId);
        } else {
            return accountRepository.save(new Account(playerId, 0.0, "EUR"));
        }

    }

    public Account getPlayerAccount(long playerId) {

        return accountRepository.getReferenceById(playerId);

    }

    @Transactional
    public Account updatePlayerAccount(Account account) {

        return accountRepository.save(account);

    }

    public void validateIfPlayerAccountExist(long playerId) {

        if (!accountRepository.existsById(playerId)) {
            throw new AccountServiceException("Player does not exist!");
        }

    }

}
