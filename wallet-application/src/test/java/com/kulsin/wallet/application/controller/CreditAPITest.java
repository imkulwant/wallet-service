package com.kulsin.wallet.application.controller;

import com.kulsin.wallet.controller.WalletResource;
import com.kulsin.wallet.core.transaction.exception.TransactionServiceException;
import com.kulsin.wallet.exception.WalletException;
import com.kulsin.wallet.model.request.WalletRequest;
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
import static com.kulsin.wallet.application.controller.ResourceTestCommon.mockCreditRequest;
import static com.kulsin.wallet.application.controller.ResourceTestCommon.mockWalletResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CreditAPITest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletResource walletResource;

    private MockMvc mockMvc;

    private static final String WALLET_CREDIT_PATH = "/api/wallet/credit";

    @BeforeEach
    void setUp() {
        mockMvc = makeMockMvc(walletResource);
    }

    @Test
    void creditTest_Success() throws Exception {
        String requestPayload = """
                {
                    "playerId": 123,
                    "amount": 5,
                    "currency": "EUR",
                    "type": "credit",
                    "sessionToken":"test-token",
                    "transactionId": 989898
                }""";

        String expectedResponse = """
                {
                    "playerId": 123,
                    "balance": 5.0,
                    "transactionId": 989898
                }""";

        when(walletService.creditPlayer(mockCreditRequest()))
                .thenReturn(mockWalletResponse());

        var request = MockMvcRequestBuilders.post(WALLET_CREDIT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).creditPlayer(mockCreditRequest());

    }

    @Test
    void creditTest_InvalidRequest_MissingSessionToken() throws Exception {
        String requestPayload = """
                {
                    "playerId": 123,
                    "amount": 5,
                    "currency": "EUR",
                    "type": "credit",
                    "transactionId": 999888
                }""";

        String expectedResponse = """
                {
                    "statusCode": "400",
                    "message": "Mandatory field sessionToken is missing"
                }""";

        var request = MockMvcRequestBuilders.post(WALLET_CREDIT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(expectedResponse));

        verifyNoInteractions(walletService);

    }

    @Test
    void creditTest_DuplicateTransaction() throws Exception {
        String requestPayload = """
                {
                    "playerId": 123,
                    "amount": 5,
                    "currency": "EUR",
                    "type": "credit",
                    "sessionToken":"test-token",
                    "transactionId": 999888
                }""";

        String expectedResponse = """
                {
                    "message": "Transaction id 999888 is not unique!",
                    "statusCode": "400"
                }""";


        when(walletService.creditPlayer(any(WalletRequest.class)))
                .thenThrow(new WalletException("Transaction id 999888 is not unique!"));

        var request = MockMvcRequestBuilders.post(WALLET_CREDIT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).creditPlayer(any(WalletRequest.class));

    }

    @Test
    void creditTest_UnExpected_TransactionServiceException() throws Exception {
        String requestPayload = """
                {
                    "playerId": 123,
                    "amount": 5,
                    "currency": "EUR",
                    "type": "credit",
                    "sessionToken":"test-token",
                    "transactionId": 999888
                }""";

        String expectedResponse = """
                {
                    "message": "Unexpected transaction service exception",
                    "statusCode": "500"
                }""";

        Mockito.when(walletService.creditPlayer(any(WalletRequest.class)))
                .thenThrow(new TransactionServiceException("Unexpected transaction service exception"));

        var request = MockMvcRequestBuilders.post(WALLET_CREDIT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().is5xxServerError())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).creditPlayer(any(WalletRequest.class));

    }

}
