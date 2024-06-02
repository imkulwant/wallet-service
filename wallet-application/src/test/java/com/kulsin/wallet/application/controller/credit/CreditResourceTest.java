package com.kulsin.wallet.application.controller.credit;

import com.kulsin.wallet.core.transaction.TransactionServiceException;
import com.kulsin.wallet.application.errorhandling.WalletException;
import com.kulsin.wallet.application.model.WalletRequest;
import com.kulsin.wallet.application.model.WalletResponse;
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

import static com.kulsin.wallet.application.controller.balance.ResourceTestCommon.makeMockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CreditResourceTest {

    @Mock
    private CreditService creditService;
    @InjectMocks
    private CreditResource creditResource;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = makeMockMvc(creditResource);
    }

    @Test
    void creditTest_Success() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 123,
                            "amount": 5,
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

        WalletRequest creditRequest = new WalletRequest(13L, 5, "EUR", 999888L);
        Mockito.when(creditService.creditPlayer(creditRequest))
                .thenReturn(new WalletResponse(123L, 5.0, 999888));

        var request = MockMvcRequestBuilders.post("/v1/wallet/credit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(creditService, times(1)).creditPlayer(creditRequest);

    }


    @Test
    void creditTest_InvalidRequest_MissingTransactionId() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 123,
                            "amount": 5,
                            "currency": "EUR"
                        }
                """;

        String expectedResponse = """
                    {
                        "errorMessage": "Mandatory field transactionId is missing",
                        "errorStatus": 400
                    }
                """;

        var request = MockMvcRequestBuilders.post("/v1/wallet/credit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verifyNoInteractions(creditService);

    }

    @Test
    void creditTest_DuplicateTransaction() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 123,
                            "amount": 5,
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

        WalletRequest creditRequest = new WalletRequest(13L, 5, "EUR", 999888L);
        Mockito.when(creditService.creditPlayer(creditRequest))
                .thenThrow(new WalletException("Transaction id 999888 is not unique!"));

        var request = MockMvcRequestBuilders.post("/v1/wallet/credit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(creditService, times(1)).creditPlayer(creditRequest);

    }

    @Test
    void creditTest_UnExpected_TransactionServiceException() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 123,
                            "amount": 5,
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

        WalletRequest creditRequest = new WalletRequest(13L, 5, "EUR", 999888L);
        Mockito.when(creditService.creditPlayer(creditRequest))
                .thenThrow(new TransactionServiceException("Unexpected transaction service exception"));

        var request = MockMvcRequestBuilders.post("/v1/wallet/credit.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(creditService, times(1)).creditPlayer(creditRequest);

    }


}