package com.kulsin.wallet.controller.debit;

import com.kulsin.wallet.core.transaction.TransactionServiceException;
import com.kulsin.wallet.errorhandling.WalletException;
import com.kulsin.wallet.model.WalletRequest;
import com.kulsin.wallet.model.WalletResponse;
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

import static com.kulsin.wallet.controller.balance.ResourceTestCommon.makeMockMvc;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DebitResourceTest {

    @Mock
    private DebitService debitService;
    @InjectMocks
    private DebitResource debitResource;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = makeMockMvc(debitResource);
    }

    @Test
    void debitTest_Success() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 123,
                            "amount": 5.0,
                            "currency": "EUR",
                            "transactionId": 999888
                        }
                """;

        String expectedResponse = """
                        {
                            "playerId": 123,
                            "balance": 5.0,
                            "transactionId": 999888
                        }
                """;

        WalletRequest debitRequest = new WalletRequest(13L, 5.0, "EUR", 999888L);
        Mockito.when(debitService.debitPlayer(debitRequest))
                .thenReturn(new WalletResponse(123L, 5.0, 999888));

        var request = MockMvcRequestBuilders.post("/v1/wallet/debit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(debitService, times(1)).debitPlayer(debitRequest);

    }

    @Test
    void debitTest_Failure_InsufficientFunds() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 123,
                            "amount": 5.0,
                            "currency": "EUR",
                            "transactionId": 999888
                        }
                """;

        String expectedResponse = """
                    {
                        "errorMessage": "Transaction declined! player has in-sufficient funds",
                        "errorStatus": 400
                    }
                """;

        WalletRequest debitRequest = new WalletRequest(13L, 5.0, "EUR", 999888L);
        Mockito.when(debitService.debitPlayer(debitRequest))
                .thenThrow(new WalletException("Transaction declined! player has in-sufficient funds"));

        var request = MockMvcRequestBuilders.post("/v1/wallet/debit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(debitService, times(1)).debitPlayer(debitRequest);

    }

    @Test
    void debitTest_Failure_DuplicateTransaction() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 123,
                            "amount": 5.0,
                            "currency": "EUR",
                            "transactionId": 999888
                        }
                """;

        String expectedResponse = """
                    {
                        "errorMessage": "Transaction id 999888 is not unique!",
                        "errorStatus": 400
                    }
                """;

        WalletRequest debitRequest = new WalletRequest(13L, 5.0, "EUR", 999888L);
        Mockito.when(debitService.debitPlayer(debitRequest))
                .thenThrow(new WalletException("Transaction id 999888 is not unique!"));

        var request = MockMvcRequestBuilders.post("/v1/wallet/debit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(debitService, times(1)).debitPlayer(debitRequest);

    }

    @Test
    void debitTest_Failure_UnExpectedError_TransactionServiceException() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 123,
                            "amount": 5.0,
                            "currency": "EUR",
                            "transactionId": 999888
                        }
                """;

        String expectedResponse = """
                    {
                        "errorMessage": "Unexpected transaction service exception",
                        "errorStatus": 500
                    }
                """;

        WalletRequest debitRequest = new WalletRequest(13L, 5.0, "EUR", 999888L);
        Mockito.when(debitService.debitPlayer(debitRequest))
                .thenThrow(new TransactionServiceException("Unexpected transaction service exception"));

        var request = MockMvcRequestBuilders.post("/v1/wallet/debit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(debitService, times(1)).debitPlayer(debitRequest);

    }

}