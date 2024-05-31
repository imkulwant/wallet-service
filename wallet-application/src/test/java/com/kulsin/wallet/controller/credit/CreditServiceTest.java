package com.kulsin.wallet.controller.credit;

import com.kulsin.accounting.account.AccountService;
import com.kulsin.accounting.transaction.Transaction;
import com.kulsin.accounting.transaction.TransactionService;
import com.kulsin.wallet.errorhandling.WalletException;
import com.kulsin.wallet.model.WalletRequest;
import com.kulsin.wallet.model.WalletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private AccountService accountService;
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private CreditService creditService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void creditPlayer_Success_ExistingPlayer() {

        WalletRequest creditRequest = new WalletRequest(123L, 5, "EUR", 999888L);

        when(transactionService.transactionExists(999888L)).thenReturn(false);
        when(accountService.accountExist(123L)).thenReturn(true);
        when(accountService.getBalance(123L)).thenReturn(5.0);


        WalletResponse response = creditService.creditPlayer(creditRequest);

        assertEquals(123L, response.getPlayerId());
        assertEquals(5.0, response.getBalance());
        assertEquals("EUR", response.getCurrency());
        assertEquals(999888L, response.getTransactionId());


        verify(accountService, times(1)).accountExist(123L);
        verify(accountService, times(2)).getBalance(123L);
        verify(accountService, times(1)).updatePlayerBalance(123L, 10.0, "EUR");
        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
        verify(transactionService, times(1)).transactionExists(999888L);

    }

    @Test
    void creditPlayer_Success_NewPlayer() {

        WalletRequest creditRequest = new WalletRequest(123L, 5, "EUR", 999888L);

        when(transactionService.transactionExists(999888L)).thenReturn(false);
        when(accountService.accountExist(123L)).thenReturn(false);
        when(accountService.getBalance(123L)).thenReturn(5.0);

        WalletResponse response = creditService.creditPlayer(creditRequest);

        assertEquals(123L, response.getPlayerId());
        assertEquals(5.0, response.getBalance());
        assertEquals("EUR", response.getCurrency());
        assertEquals(999888L, response.getTransactionId());

        verify(accountService, times(1)).accountExist(123L);
        verify(accountService, times(1)).getBalance(123L);
        verify(accountService, times(1)).updatePlayerBalance(123L, 5.0, "EUR");
        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
        verify(transactionService, times(1)).transactionExists(999888L);

    }

    @Test
    void creditPlayer_Failure_DuplicateTransaction() {

        WalletRequest creditRequest = new WalletRequest(123L, 5, "EUR", 999888L);

        when(transactionService.transactionExists(999888L)).thenReturn(true);

        Assertions.assertThrows(WalletException.class, () -> creditService.creditPlayer(creditRequest));

        verify(transactionService, times(1)).transactionExists(999888L);
        verifyNoMoreInteractions(accountService, transactionService);

    }

}