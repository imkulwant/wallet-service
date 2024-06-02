package com.kulsin.wallet.application.controller;

import com.kulsin.wallet.application.service.WalletService;
import com.kulsin.wallet.application.errorhandling.WalletException;
import com.kulsin.wallet.application.model.WalletRequest;
import com.kulsin.wallet.application.model.WalletResponse;
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
class WalletResourceTest {

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

    @Test
    void creditTest_Success() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 123,
                            "amount": 5,
                            "currency": "EUR",
                            "transactionId": 78975456
                        }
                """;

        String expectedResponse = """
                        {
                            "playerId": 123,
                            "balance": 5.0,
                            "transactionId": 78975456
                        }
                """;

        WalletRequest creditRequest = new WalletRequest(13L, 5, "EUR", 999888L);
        Mockito.when(walletService.creditPlayer(creditRequest))
                .thenReturn(new WalletResponse(123L, 5.0, 999888));

        var request = MockMvcRequestBuilders.post("/api/wallet/credit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).creditPlayer(creditRequest);

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

        var request = MockMvcRequestBuilders.post("/api/wallet/credit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
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
        Mockito.when(walletService.creditPlayer(creditRequest))
                .thenThrow(new WalletException("Transaction id 999888 is not unique!"));

        var request = MockMvcRequestBuilders.post("/api/wallet/credit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).creditPlayer(creditRequest);

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
        Mockito.when(walletService.creditPlayer(creditRequest))
                .thenThrow(new TransactionServiceException("Unexpected transaction service exception"));

        var request = MockMvcRequestBuilders.post("/api/wallet/credit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).creditPlayer(creditRequest);

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
        Mockito.when(walletService.debitPlayer(debitRequest))
                .thenReturn(new WalletResponse(123L, 5.0, 999888));

        var request = MockMvcRequestBuilders.post("/api/wallet/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).debitPlayer(debitRequest);

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
        Mockito.when(walletService.debitPlayer(debitRequest))
                .thenThrow(new WalletException("Transaction declined! player has in-sufficient funds"));

        var request = MockMvcRequestBuilders.post("/api/wallet/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).debitPlayer(debitRequest);

    }

    @Test
    void debitTest_Failure_DuplicateTransaction() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 123,
                            "amount": 5.0,
                            "currency": "EUR",
                            "transactionId": 132456
                        }
                """;

        String expectedResponse = """
                    {
                        "errorMessage": "Transaction id 999888 is not unique!",
                        "errorStatus": 400
                    }
                """;

        WalletRequest debitRequest = new WalletRequest(13L, 5.0, "EUR", 999888L);
        Mockito.when(walletService.debitPlayer(debitRequest))
                .thenThrow(new WalletException("Transaction id 999888 is not unique!"));

        var request = MockMvcRequestBuilders.post("/api/wallet/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).debitPlayer(debitRequest);

    }

    @Test
    void debitTest_Failure_UnExpectedError_TransactionServiceException() throws Exception {
        String requestPayload = """
                        {
                            "playerId": 123,
                            "amount": 5.0,
                            "currency": "EUR",
                            "transactionId": 78945664
                        }
                """;

        String expectedResponse = """
                    {
                        "errorMessage": "Unexpected transaction service exception",
                        "errorStatus": 500
                    }
                """;

        WalletRequest debitRequest = new WalletRequest(13L, 5.0, "EUR", 999888L);
        Mockito.when(walletService.debitPlayer(debitRequest))
                .thenThrow(new TransactionServiceException("Unexpected transaction service exception"));

        var request = MockMvcRequestBuilders.post("/api/wallet/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).debitPlayer(debitRequest);

    }

    @Test
    void playerHistoryTest_Success() throws Exception {

        String expectedResponse = """
                    {
                        "transactions" :
                            [
                                {
                                    "transactionId": 789789,
                                    "playerId": 123,
                                    "amount": 0.85,
                                    "transactionType": "DEBIT",
                                    "timestamp": "2022-05-11T23:28:06.492311994Z"
                                },
                                {
                                    "transactionId": 4568797,
                                    "playerId": 123,
                                    "amount": 2.0,
                                    "transactionType": "CREDIT",
                                    "timestamp": "2022-05-11T20:00:17.386492513Z"
                                }
                            ]
                    }
                """;

        List<Transaction> response = List.of(
                new Transaction(9082348L, 123L, 0.85, "DEBIT", "2022-05-11T23:28:06.492311994Z"),
                new Transaction(65646456L, 123L, 2.0, "CREDIT", "2022-05-11T20:00:17.386492513Z")
        );
        Mockito.when(accountService.accountExist(123L)).thenReturn(true);
        Mockito.when(transactionService.getTransactions(123L)).thenReturn(response);

        var request = MockMvcRequestBuilders.get("/api/wallet/history?playerId=123");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(accountService, times(1)).accountExist(123L);
        verify(transactionService, times(1)).getTransactions(123L);

    }

    @Test
    void playerHistoryTest_Failure_InvalidPlayerId() throws Exception {

        String expectedResponse = """
                    {
                        "errorMessage": "Invalid player id! player account doesn't exists",
                        "errorStatus": 400
                    }
                """;

        Mockito.when(accountService.accountExist(123L)).thenReturn(false);

        var request = MockMvcRequestBuilders.get("/api/wallet/history?playerId=123");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(accountService, times(1)).accountExist(123L);
        verifyNoInteractions(transactionService);

    }

}
