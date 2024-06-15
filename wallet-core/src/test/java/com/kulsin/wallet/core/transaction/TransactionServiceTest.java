package com.kulsin.wallet.core.transaction;

import com.kulsin.wallet.core.account.AccountService;
import com.kulsin.wallet.core.transaction.entity.Transaction;
import com.kulsin.wallet.core.transaction.exception.TransactionServiceException;
import com.kulsin.wallet.core.transaction.model.TransactionRequest;
import com.kulsin.wallet.core.transaction.model.TransactionResponse;
import com.kulsin.wallet.core.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static com.kulsin.wallet.core.TestMockUtil.PLAYER_ID;
import static com.kulsin.wallet.core.TestMockUtil.mockCreditRequest;
import static com.kulsin.wallet.core.TestMockUtil.mockDebitRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionServiceImpl(accountService, transactionRepository);
    }

    @Test
    public void testBalance() {
        double balance = 100.0;

        when(accountService.accountBalance(PLAYER_ID)).thenReturn(balance);

        TransactionResponse response = transactionService.balance(PLAYER_ID);

        assertEquals(PLAYER_ID, response.getPlayerId());
        assertEquals(balance, response.getBalance());
        verify(accountService, times(1)).accountBalance(PLAYER_ID);
    }

    @Test
    public void testTransactCredit() {
        TransactionRequest request = mockCreditRequest();

        when(transactionRepository.existsById(request.getTransactionId())).thenReturn(false);
        when(accountService.creditAccount(request.getPlayerId(), request.getAmount())).thenReturn(150.0);

        TransactionResponse response = transactionService.transact(request);

        assertEquals(request.getPlayerId(), response.getPlayerId());
        assertEquals(150.0, response.getBalance());
        assertEquals(request.getTransactionId(), response.getTransactionId());
        verify(transactionRepository, times(1)).existsById(request.getTransactionId());
        verify(accountService, times(1)).creditAccount(request.getPlayerId(), request.getAmount());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testTransactDebit() {
        TransactionRequest request = mockDebitRequest();

        when(transactionRepository.existsById(request.getTransactionId())).thenReturn(false);
        when(accountService.debitAccount(request.getPlayerId(), request.getAmount())).thenReturn(70.0);

        TransactionResponse response = transactionService.transact(request);

        assertEquals(request.getPlayerId(), response.getPlayerId());
        assertEquals(70.0, response.getBalance());
        assertEquals(request.getTransactionId(), response.getTransactionId());
        verify(transactionRepository, times(1)).existsById(request.getTransactionId());
        verify(accountService, times(1)).debitAccount(request.getPlayerId(), request.getAmount());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testTransactTransactionNotUnique() {

        TransactionRequest request = mockCreditRequest();

        when(transactionRepository.existsById(request.getTransactionId())).thenReturn(true);

        TransactionServiceException exception = assertThrows(TransactionServiceException.class, () -> transactionService.transact(request));

        assertEquals("Transaction is not unique", exception.getMessage());
        verify(transactionRepository, times(1)).existsById(request.getTransactionId());
        verify(accountService, times(0)).creditAccount(anyLong(), anyDouble());
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    public void testPlayerTransactions() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setPlayerId(PLAYER_ID);
        transaction.setAmount(50.0);
        transaction.setTransactionType("CREDIT");
        transaction.setTimestamp(Instant.now().toString());

        when(transactionRepository.findAllByPlayerId(PLAYER_ID)).thenReturn(Collections.singletonList(transaction));

        List<Transaction> transactions = transactionService.playerTransactions(PLAYER_ID);

        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
        verify(transactionRepository, times(1)).findAllByPlayerId(PLAYER_ID);
    }

}