package com.kulsin.wallet.application.controller;

import com.kulsin.wallet.application.errorhandling.WalletException;
import com.kulsin.wallet.application.model.WalletRequest;
import com.kulsin.wallet.application.model.WalletResponse;
import com.kulsin.wallet.application.service.WalletService;
import com.kulsin.wallet.core.account.AccountService;
import com.kulsin.wallet.core.account.AccountServiceException;
import com.kulsin.wallet.core.transaction.Transaction;
import com.kulsin.wallet.core.transaction.TransactionService;
import com.kulsin.wallet.core.transaction.TransactionServiceException;
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

import java.util.List;

import static com.kulsin.wallet.application.controller.ResourceTestCommon.makeMockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BalanceAPITest {

    @Mock
    private AccountService accountService;
    @Mock
    private TransactionService transactionService;
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
    void getBalanceTest() throws Exception {
        String getBalanceRequestPayload = """
                        {
                            "playerId": 123
                        }
                """;

        String expectedBalanceResponse = """
                        {
                            "playerId": 123,
                            "balance": 5.0,
                            "currency": "EUR"
                        }
                """;

        Mockito.when(walletService.playerBalance(123L))
                .thenReturn(new WalletResponse(123L, 5.0, 345678087));

        var request = MockMvcRequestBuilders.post("/api/wallet/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBalanceRequestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBalanceResponse));

        verify(walletService, times(1)).playerBalance(123L);

    }

    @Test
    void getBalanceTest_InvalidRequest() throws Exception {
        String getBalanceRequestPayload = """
                    {
                        "customerId": 123
                    }
                """;

        String expectedBalanceResponse = """
                    {
                        "errorMessage": "Mandatory field playerId is missing",
                        "errorStatus": 400
                    }
                """;

        var request = MockMvcRequestBuilders.post("/api/wallet/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBalanceRequestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBalanceResponse));

        verifyNoInteractions(walletService);

    }

    @Test
    void getBalanceTest_InvalidPlayerId() throws Exception {
        String getBalanceRequestPayload = """
                    {
                        "playerId": 123
                    }
                """;

        String expectedBalanceResponse = """
                    {
                        "errorMessage": "Invalid player id! player account doesn't exists",
                        "errorStatus": 400
                    }
                """;

        Mockito.when(walletService.playerBalance(123L))
                .thenThrow(new WalletException("Invalid player id! player account doesn't exists"));

        var request = MockMvcRequestBuilders.post("/api/wallet/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBalanceRequestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBalanceResponse));

        verify(walletService, times(1)).playerBalance(123L);

    }

    @Test
    void getBalanceTest_UnExpected_AccountServiceException() throws Exception {
        String getBalanceRequestPayload = """
                    {
                        "playerId": 123
                    }
                """;

        String expectedBalanceResponse = """
                    {
                        "errorMessage": "Unexpected error occurred while fetching player balance",
                        "errorStatus": 500
                    }
                """;

        Mockito.when(walletService.playerBalance(123L))
                .thenThrow(new AccountServiceException("Unexpected error occurred while fetching player balance"));

        var request = MockMvcRequestBuilders.post("/api/wallet/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBalanceRequestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBalanceResponse));

        verify(walletService, times(1)).playerBalance(123L);

    }

}
