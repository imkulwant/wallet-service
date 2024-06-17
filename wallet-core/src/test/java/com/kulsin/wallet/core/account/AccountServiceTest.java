package com.kulsin.wallet.core.account;

import com.kulsin.wallet.core.account.entity.Account;
import com.kulsin.wallet.core.account.exception.AccountServiceException;
import com.kulsin.wallet.core.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.kulsin.wallet.core.TestMockUtil.PLAYER_ID;
import static com.kulsin.wallet.core.TestMockUtil.mockAccount;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountServiceImpl(accountRepository);
    }

    @Test
    void accountBalance() {
        when(accountRepository.findById(PLAYER_ID)).thenReturn(mockAccount());

        assertEquals(100.0, accountService.accountBalance(1L));
    }

    @Test
    public void testCreditAccount() {
        when(accountRepository.findById(PLAYER_ID)).thenReturn(mockAccount());

        double newBalance = accountService.creditAccount(PLAYER_ID, 50.0);

        assertEquals(150.0, newBalance);
        verify(accountRepository, times(1)).findById(PLAYER_ID);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testDebitAccount() {
        when(accountRepository.findById(PLAYER_ID)).thenReturn(mockAccount());

        double newBalance = accountService.debitAccount(PLAYER_ID, 50.0);

        assertEquals(50.0, newBalance);
        verify(accountRepository, times(1)).findById(PLAYER_ID);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testDebitAccountInsufficientFunds() {
        when(accountRepository.findById(PLAYER_ID)).thenReturn(mockAccount());

        AccountServiceException exception = assertThrows(AccountServiceException.class,
                () -> accountService.debitAccount(PLAYER_ID, 150.0));

        assertEquals("Debit declined! player has in-sufficient funds", exception.getMessage());
        verify(accountRepository, times(1)).findById(PLAYER_ID);
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    public void testGetPlayerAccountNotFound() {
        when(accountRepository.findById(PLAYER_ID)).thenReturn(Optional.empty());

        AccountServiceException exception = assertThrows(AccountServiceException.class,
                () -> accountService.accountBalance(PLAYER_ID));

        assertEquals("Player does not exist!", exception.getMessage());
        verify(accountRepository, times(1)).findById(PLAYER_ID);
    }

}