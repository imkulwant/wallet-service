package com.kulsin.accounting.transaction;

import com.kulsin.wallet.core.transaction.Transaction;
import com.kulsin.wallet.core.transaction.TransactionRepository;
import com.kulsin.wallet.core.transaction.TransactionService;
import com.kulsin.wallet.core.transaction.TransactionServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionService transactionService;

    @Test
    void saveTransactionTest_Success() {

        Transaction transaction = new Transaction(9988L, 123L, 1.5,
                "DEBIT", Instant.now().toString());

        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction response = transactionService.saveTransaction(transaction);

        assertEquals(transaction, response);
        verify(transactionRepository, Mockito.times(1)).save(transaction);
    }

    @Test
    void getPlayerTransactionsTest_Success() {

        Transaction t1 = new Transaction(9988L, 123L, 1.5,
                "DEBIT", Instant.now().toString());

        Transaction t2 = new Transaction(9458L, 321L, 2.5,
                "CREDIT", Instant.now().toString());


        when(transactionRepository.findAll()).thenReturn(List.of(t1, t2));

        List<Transaction> response = transactionService.getPlayerTransactions(123L);

        assertTrue(response.contains(t1));
        verify(transactionRepository, Mockito.times(1)).findAll();


    }

    @Test
    void transactionExistsTest_Success() {

        when(transactionRepository.existsById(9988L)).thenReturn(true);

        assertTrue(transactionService.validateTransactionIsUnique(9988L));
        verify(transactionRepository, Mockito.times(1)).existsById(9988L);

    }

    @Test
    void saveTransactionTest_Failure_TransactionServiceException() {

        Transaction transaction = new Transaction(9988L, 123L, 1.5,
                "DEBIT", Instant.now().toString());

        when(transactionRepository.save(transaction)).thenThrow(new RuntimeException());

        assertThrows(TransactionServiceException.class, () -> transactionService.saveTransaction(transaction));

        verify(transactionRepository, Mockito.times(1)).save(transaction);
    }

    @Test
    void getPlayerTransactionsTest_Failure_TransactionServiceException() {

        when(transactionRepository.findAll()).thenThrow(new RuntimeException());

        assertThrows(TransactionServiceException.class, () -> transactionService.getPlayerTransactions(123L));

        verify(transactionRepository, Mockito.times(1)).findAll();

    }

    @Test
    void transactionExistsTest_Failure_TransactionServiceException() {

        when(transactionRepository.existsById(9988L)).thenThrow(new RuntimeException());

        assertThrows(TransactionServiceException.class, () -> transactionService.validateTransactionIsUnique(9988L));

        verify(transactionRepository, Mockito.times(1)).existsById(9988L);

    }
}