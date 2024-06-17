package com.kulsin.wallet.application.service;

import com.kulsin.wallet.converter.TxnResponseToWalletResponse;
import com.kulsin.wallet.converter.WalletRequestToTxnRequestConverter;
import com.kulsin.wallet.core.session.PlayerSessionService;
import com.kulsin.wallet.core.transaction.TransactionService;
import com.kulsin.wallet.core.transaction.entity.Transaction;
import com.kulsin.wallet.core.transaction.model.TransactionRequest;
import com.kulsin.wallet.model.request.BalanceRequest;
import com.kulsin.wallet.model.request.WalletRequest;
import com.kulsin.wallet.model.response.WalletResponse;
import com.kulsin.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.support.GenericConversionService;

import java.time.Instant;
import java.util.List;

import static com.kulsin.wallet.application.controller.ResourceTestCommon.PLAYER_ID;
import static com.kulsin.wallet.application.controller.ResourceTestCommon.mockCreditRequest;
import static com.kulsin.wallet.application.controller.ResourceTestCommon.mockDebitRequest;
import static com.kulsin.wallet.application.controller.ResourceTestCommon.mockTransactionResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private PlayerSessionService sessionService;

    @Mock
    private TransactionService transactionService;

    private WalletService walletService;

    @BeforeEach
    void beforeEach() {
        GenericConversionService conversionService = new GenericConversionService();
        conversionService.addConverter(new TxnResponseToWalletResponse());
        conversionService.addConverter(new WalletRequestToTxnRequestConverter());

        walletService = new WalletService(conversionService, sessionService, transactionService);
    }

    @Test
    public void testAuthenticate() {
        // Given
        BalanceRequest request = new BalanceRequest(PLAYER_ID, "sessionToken");
        doNothing().when(sessionService).validateIfSessionIsActive(request.getSessionToken());
        when(transactionService.balance(request.getPlayerId())).thenReturn(mockTransactionResponse());

        // When
        WalletResponse response = walletService.authenticate(request);

        // Then
        assertNotNull(response);
        assertEquals(request.getPlayerId(), response.getPlayerId());
    }

    @Test
    public void testPlayerBalance() {
        // Given
        BalanceRequest request = new BalanceRequest(PLAYER_ID, "sessionToken");
        doNothing().when(sessionService).validateIfSessionIsActive(request.getSessionToken());

        when(transactionService.balance(request.getPlayerId())).thenReturn(mockTransactionResponse());

        // When
        WalletResponse response = walletService.playerBalance(request);

        // Then
        assertNotNull(response);
        assertEquals(request.getPlayerId(), response.getPlayerId());
    }

    @Test
    public void testCreditPlayer() {
        // Given
        WalletRequest request = mockCreditRequest();
        doNothing().when(sessionService).validateIfSessionIsActive(request.getSessionToken());
        when(transactionService.transact(any(TransactionRequest.class))).thenReturn(mockTransactionResponse());

        // When
        WalletResponse response = walletService.creditPlayer(request);

        // Then
        assertNotNull(response);
        assertEquals(request.getPlayerId(), response.getPlayerId());
        assertEquals(100.0, response.getBalance());
    }

    @Test
    public void testDebitPlayer() {
        // Given
        WalletRequest request = mockDebitRequest();
        doNothing().when(sessionService).validateIfSessionIsActive(request.getSessionToken());
        when(transactionService.transact(any(TransactionRequest.class))).thenReturn(mockTransactionResponse());

        // When
        WalletResponse response = walletService.debitPlayer(request);

        // Then
        assertNotNull(response);
        assertEquals(request.getPlayerId(), response.getPlayerId());
        assertEquals(100.0, response.getBalance());
    }

    @Test
    public void testPlayerHistory() {
        // Given
        List<Transaction> transactions = List.of(

                Transaction.builder()
                        .transactionId(9898L)
                        .playerId(PLAYER_ID)
                        .amount(120.00)
                        .timestamp(Instant.now().toString())
                        .transactionType("credit")
                        .build()
                ,
                Transaction.builder()
                        .transactionId(9999)
                        .playerId(PLAYER_ID)
                        .amount(20.00)
                        .timestamp(Instant.now().toString())
                        .transactionType("debit")
                        .build()
        );

        when(transactionService.playerTransactions(PLAYER_ID)).thenReturn(transactions);

        // When
        List<Transaction> response = walletService.playerHistory(PLAYER_ID);

        // Then
        assertNotNull(response);
        assertEquals(transactions, response);

    }

}