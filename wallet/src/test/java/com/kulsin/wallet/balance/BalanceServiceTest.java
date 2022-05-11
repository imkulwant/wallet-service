package com.kulsin.wallet.balance;

import com.kulsin.accounting.account.AccountService;
import com.kulsin.wallet.errorhandling.WalletException;
import com.kulsin.wallet.model.WalletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private AccountService accountService;
    @InjectMocks
    private BalanceService balanceService;

    @Test
    void playerBalanceTest_Success() {

        when(accountService.accountExist(123L)).thenReturn(true);
        when(accountService.getBalance(123L)).thenReturn(23.0);

        WalletResponse response = balanceService.playerBalance(123L);

        assertEquals(123L, response.getPlayerId());
        assertEquals(23.0, response.getBalance());
        assertEquals("EUR", response.getCurrency());

        verify(accountService, times(1)).accountExist(123L);
        verify(accountService, times(1)).getBalance(123L);

    }

    @Test
    void playerBalanceTest_InvalidPlayer() {

        when(accountService.accountExist(123L)).thenReturn(false);

        Assertions.assertThrows(WalletException.class, () -> balanceService.playerBalance(123L));

        verify(accountService, times(1)).accountExist(123L);
        verifyNoMoreInteractions(accountService);

    }

    @Test
    void playerBalanceTest_UnExpectedError() {

        when(accountService.accountExist(123L)).thenReturn(true);
        when(accountService.getBalance(123L)).thenThrow(new RuntimeException("Unexpected error"));

        Assertions.assertThrows(RuntimeException.class, () -> balanceService.playerBalance(123L));

        verify(accountService, times(1)).accountExist(123L);
        verify(accountService, times(1)).getBalance(123L);

    }


}