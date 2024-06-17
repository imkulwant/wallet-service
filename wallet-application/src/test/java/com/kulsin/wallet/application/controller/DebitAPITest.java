package com.kulsin.wallet.application.controller;

import com.kulsin.wallet.controller.WalletResource;
import com.kulsin.wallet.core.transaction.exception.TransactionServiceException;
import com.kulsin.wallet.exception.WalletException;
import com.kulsin.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.kulsin.wallet.application.controller.ResourceTestCommon.makeMockMvc;
import static com.kulsin.wallet.application.controller.ResourceTestCommon.mockDebitRequest;
import static com.kulsin.wallet.application.controller.ResourceTestCommon.mockWalletResponse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DebitAPITest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletResource walletResource;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = makeMockMvc(walletResource);
    }

    @Test
    void debitTest_Success() throws Exception {
        String requestPayload = """
                {
                    "playerId": 123,
                    "amount": 5.0,
                    "type": "debit",
                    "currency": "EUR",
                    "sessionToken":"test-token",
                    "transactionId": 989898
                }""";

        String expectedResponse = """
                {
                    "playerId": 123,
                    "balance": 5.0,
                    "transactionId": 989898
                }""";

        Mockito.when(walletService.debitPlayer(mockDebitRequest()))
                .thenReturn(mockWalletResponse());

        var request = MockMvcRequestBuilders.post("/api/wallet/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).debitPlayer(mockDebitRequest());

    }

    @Test
    void debitTest_Failure_InsufficientFunds() throws Exception {
        String requestPayload = """
                {
                    "playerId": 123,
                    "amount": 5.0,
                    "type": "debit",
                    "currency": "EUR",
                    "sessionToken":"test-token",
                    "transactionId": 989898
                }""";

        String expectedResponse = """
                {
                    "message": "Transaction declined! player has in-sufficient funds",
                    "statusCode": "400"
                }""";

        Mockito.when(walletService.debitPlayer(mockDebitRequest()))
                .thenThrow(new WalletException("Transaction declined! player has in-sufficient funds"));

        var request = MockMvcRequestBuilders.post("/api/wallet/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).debitPlayer(mockDebitRequest());

    }

    @Test
    void debitTest_Failure_DuplicateTransaction() throws Exception {
        String requestPayload = """
                {
                    "playerId": 123,
                    "amount": 5.0,
                    "type": "debit",
                    "currency": "EUR",
                    "sessionToken":"test-token",
                    "transactionId": 989898
                }""";

        String expectedResponse = """
                {
                    "message": "Transaction id 999888 is not unique!",
                    "statusCode": "400"
                }""";

        Mockito.when(walletService.debitPlayer(mockDebitRequest()))
                .thenThrow(new WalletException("Transaction id 999888 is not unique!"));

        var request = MockMvcRequestBuilders.post("/api/wallet/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).debitPlayer(mockDebitRequest());

    }

    @Test
    void debitTest_Failure_UnExpectedError_TransactionServiceException() throws Exception {
        String requestPayload = """
                {
                    "playerId": 123,
                    "amount": 5.0,
                    "type": "debit",
                    "currency": "EUR",
                    "sessionToken":"test-token",
                    "transactionId": 989898
                }""";

        String expectedResponse = """
                {
                    "message": "Unexpected transaction service exception",
                    "statusCode": "500"
                }""";

        Mockito.when(walletService.debitPlayer(mockDebitRequest()))
                .thenThrow(new TransactionServiceException("Unexpected transaction service exception"));

        var request = MockMvcRequestBuilders.post("/api/wallet/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().is5xxServerError())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).debitPlayer(mockDebitRequest());

    }

}
