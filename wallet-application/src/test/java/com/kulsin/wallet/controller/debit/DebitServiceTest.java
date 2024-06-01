package com.kulsin.wallet.controller.debit;

import com.kulsin.accounting.account.AccountService;
import com.kulsin.accounting.transaction.Transaction;
import com.kulsin.accounting.transaction.TransactionService;
import com.kulsin.wallet.errorhandling.WalletException;
import com.kulsin.wallet.model.WalletRequest;
import com.kulsin.wallet.model.WalletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebitServiceTest {

    @Mock
    private AccountService accountService;
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private DebitService debitService;

    @Test
    void debitPlayerTest_Success() {
        WalletRequest debitRequest = new WalletRequest(123L, 5.0, "EUR", 999888L);
        Mockito.when(accountService.accountExist(123L)).thenReturn(true);
        Mockito.when(accountService.getBalance(123L)).thenReturn(10.0);
        Mockito.when(transactionService.transactionExists(999888L)).thenReturn(false);

        WalletResponse response = debitService.debitPlayer(debitRequest);

        assertEquals(123L, response.getPlayerId());
        assertEquals(5.0, response.getBalance());
        assertEquals("EUR", response.getCurrency());
        assertEquals(999888L, response.getTransactionId());

        verify(accountService, times(1)).accountExist(123L);
        verify(accountService, times(2)).getBalance(123L);
        verify(accountService, times(1)).updatePlayerBalance(123L, 5.0, "EUR");
        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
        verify(transactionService, times(1)).transactionExists(999888L);

    }

    @Test
    void debitPlayerTest_Failure_InSufficient_Funds() {
        WalletRequest debitRequest = new WalletRequest(123L, 5.0, "EUR", 999888L);
        Mockito.when(accountService.accountExist(123L)).thenReturn(true);
        Mockito.when(accountService.getBalance(123L)).thenReturn(1.0);
        Mockito.when(transactionService.transactionExists(999888L)).thenReturn(false);

        Assertions.assertThrows(WalletException.class, () -> debitService.debitPlayer(debitRequest));

        verify(accountService, times(1)).accountExist(123L);
        verify(accountService, times(1)).getBalance(123L);
        verify(transactionService, times(1)).transactionExists(999888L);
        verifyNoMoreInteractions(accountService, transactionService);

    }

    @Test
    void debitPlayerTest_Failure_DuplicateTransaction() {
        WalletRequest debitRequest = new WalletRequest(123L, 5.0, "EUR", 999888L);
        Mockito.when(accountService.accountExist(123L)).thenReturn(true);
        Mockito.when(transactionService.transactionExists(999888L)).thenReturn(true);

        Assertions.assertThrows(WalletException.class, () -> debitService.debitPlayer(debitRequest));

        verify(accountService, times(1)).accountExist(123L);
        verify(transactionService, times(1)).transactionExists(999888L);
        verifyNoMoreInteractions(accountService, transactionService);

    }

    @Test
    void debitPlayerTest_Failure_InvalidPlayer() {
        WalletRequest debitRequest = new WalletRequest(123L, 5.0, "EUR", 999888L);
        Mockito.when(accountService.accountExist(123L)).thenReturn(false);

        Assertions.assertThrows(WalletException.class, () -> debitService.debitPlayer(debitRequest));

        verify(accountService, times(1)).accountExist(123L);
        verifyNoMoreInteractions(accountService, transactionService);

    }

}