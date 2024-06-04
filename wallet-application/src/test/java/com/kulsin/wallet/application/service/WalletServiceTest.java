package com.kulsin.wallet.application.service;

import com.kulsin.wallet.errorhandling.WalletException;
import com.kulsin.wallet.model.WalletRequest;
import com.kulsin.wallet.model.WalletResponse;
import com.kulsin.wallet.core.account.AccountService;
import com.kulsin.wallet.core.transaction.Transaction;
import com.kulsin.wallet.core.transaction.TransactionService;
import com.kulsin.wallet.service.WalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private AccountService accountService;
    @InjectMocks
    private WalletService walletService;

    @Mock
    private TransactionService transactionService;

    @Test
    void playerBalanceTest_Success() {

        when(accountService.accountExist(123L)).thenReturn(true);
        when(accountService.getPlayerAccount(123L)).thenReturn(23.0);

        WalletResponse response = walletService.playerBalance(123L);

        assertEquals(123L, response.getPlayerId());
        assertEquals(23.0, response.getBalance());
        assertEquals("EUR", response.getCurrency());

        verify(accountService, times(1)).accountExist(123L);
        verify(accountService, times(1)).getPlayerAccount(123L);

    }

    @Test
    void playerBalanceTest_InvalidPlayer() {

        when(accountService.accountExist(123L)).thenReturn(false);

        Assertions.assertThrows(WalletException.class, () -> walletService.playerBalance(123L));

        verify(accountService, times(1)).accountExist(123L);
        verifyNoMoreInteractions(accountService);

    }

    @Test
    void playerBalanceTest_UnExpectedError() {

        when(accountService.accountExist(123L)).thenReturn(true);
        when(accountService.getPlayerAccount(123L)).thenThrow(new RuntimeException("Unexpected error"));

        Assertions.assertThrows(RuntimeException.class, () -> walletService.playerBalance(123L));

        verify(accountService, times(1)).accountExist(123L);
        verify(accountService, times(1)).getPlayerAccount(123L);

    }

    @Test
    void creditPlayer_Success_ExistingPlayer() {

        WalletRequest creditRequest = new WalletRequest(123L, 5, "EUR", 999888L);

        when(transactionService.validateTransactionIsUnique(999888L)).thenReturn(false);
        when(accountService.accountExist(123L)).thenReturn(true);
        when(accountService.getPlayerAccount(123L)).thenReturn(5.0);


        WalletResponse response = walletService.creditPlayer(creditRequest);

        assertEquals(123L, response.getPlayerId());
        assertEquals(5.0, response.getBalance());
        assertEquals("EUR", response.getCurrency());
        assertEquals(999888L, response.getTransactionId());


        verify(accountService, times(1)).accountExist(123L);
        verify(accountService, times(2)).getPlayerAccount(123L);
        verify(accountService, times(1)).updatePlayerAccount(123L, 10.0, "EUR");
        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
        verify(transactionService, times(1)).validateTransactionIsUnique(999888L);

    }

    @Test
    void creditPlayer_Success_NewPlayer() {

        WalletRequest creditRequest = new WalletRequest(123L, 5, "EUR", 999888L);

        when(transactionService.validateTransactionIsUnique(999888L)).thenReturn(false);
        when(accountService.accountExist(123L)).thenReturn(false);
        when(accountService.getPlayerAccount(123L)).thenReturn(5.0);

        WalletResponse response = walletService.creditPlayer(creditRequest);

        assertEquals(123L, response.getPlayerId());
        assertEquals(5.0, response.getBalance());
        assertEquals("EUR", response.getCurrency());
        assertEquals(999888L, response.getTransactionId());

        verify(accountService, times(1)).accountExist(123L);
        verify(accountService, times(1)).getPlayerAccount(123L);
        verify(accountService, times(1)).updatePlayerAccount(123L, 5.0, "EUR");
        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
        verify(transactionService, times(1)).validateTransactionIsUnique(999888L);

    }

    @Test
    void creditPlayer_Failure_DuplicateTransaction() {

        WalletRequest creditRequest = new WalletRequest(123L, 5, "EUR", 999888L);

        when(transactionService.validateTransactionIsUnique(999888L)).thenReturn(true);

        Assertions.assertThrows(WalletException.class, () -> walletService.creditPlayer(creditRequest));

        verify(transactionService, times(1)).validateTransactionIsUnique(999888L);
        verifyNoMoreInteractions(accountService, transactionService);

    }

    @Test
    void debitPlayerTest_Success() {
        WalletRequest debitRequest = new WalletRequest(123L, 5.0, "EUR", 999888L);
        Mockito.when(accountService.accountExist(123L)).thenReturn(true);
        Mockito.when(accountService.getPlayerAccount(123L)).thenReturn(10.0);
        Mockito.when(transactionService.validateTransactionIsUnique(999888L)).thenReturn(false);

        WalletResponse response = walletService.debitPlayer(debitRequest);

        assertEquals(123L, response.getPlayerId());
        assertEquals(5.0, response.getBalance());
        assertEquals("EUR", response.getCurrency());
        assertEquals(999888L, response.getTransactionId());

        verify(accountService, times(1)).accountExist(123L);
        verify(accountService, times(2)).getPlayerAccount(123L);
        verify(accountService, times(1)).updatePlayerAccount(123L, 5.0, "EUR");
        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
        verify(transactionService, times(1)).validateTransactionIsUnique(999888L);

    }

    @Test
    void debitPlayerTest_Failure_InSufficient_Funds() {
        WalletRequest debitRequest = new WalletRequest(123L, 5.0, "EUR", 999888L);
        Mockito.when(accountService.accountExist(123L)).thenReturn(true);
        Mockito.when(accountService.getPlayerAccount(123L)).thenReturn(1.0);
        Mockito.when(transactionService.validateTransactionIsUnique(999888L)).thenReturn(false);

        Assertions.assertThrows(WalletException.class, () -> walletService.debitPlayer(debitRequest));

        verify(accountService, times(1)).accountExist(123L);
        verify(accountService, times(1)).getPlayerAccount(123L);
        verify(transactionService, times(1)).validateTransactionIsUnique(999888L);
        verifyNoMoreInteractions(accountService, transactionService);

    }

    @Test
    void debitPlayerTest_Failure_DuplicateTransaction() {
        WalletRequest debitRequest = new WalletRequest(123L, 5.0, "EUR", 999888L);
        Mockito.when(accountService.accountExist(123L)).thenReturn(true);
        Mockito.when(transactionService.validateTransactionIsUnique(999888L)).thenReturn(true);

        Assertions.assertThrows(WalletException.class, () -> walletService.debitPlayer(debitRequest));

        verify(accountService, times(1)).accountExist(123L);
        verify(transactionService, times(1)).validateTransactionIsUnique(999888L);
        verifyNoMoreInteractions(accountService, transactionService);

    }

    @Test
    void debitPlayerTest_Failure_InvalidPlayer() {
        WalletRequest debitRequest = new WalletRequest(123L, 5.0, "EUR", 999888L);
        Mockito.when(accountService.accountExist(123L)).thenReturn(false);

        Assertions.assertThrows(WalletException.class, () -> walletService.debitPlayer(debitRequest));

        verify(accountService, times(1)).accountExist(123L);
        verifyNoMoreInteractions(accountService, transactionService);

    }

}