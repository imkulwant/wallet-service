package com.kulsin.wallet.application.controller;

import com.kulsin.wallet.controller.WalletResource;
import com.kulsin.wallet.core.account.exception.AccountServiceException;
import com.kulsin.wallet.core.transaction.entity.Transaction;
import com.kulsin.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.kulsin.wallet.application.controller.ResourceTestCommon.makeMockMvc;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@ExtendWith(MockitoExtension.class)
class HistoryAPITest {

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
    void playerHistoryTest_Success() throws Exception {

        String expectedResponse = """
                [
                    {
                        "transactionId": 9082348,
                        "playerId": 123,
                        "amount": 0.85,
                        "transactionType": "DEBIT",
                        "timestamp": "2022-05-11T23:28:06.492311994Z"
                    },
                    {
                        "transactionId": 65646456,
                        "playerId": 123,
                        "amount": 2.0,
                        "transactionType": "CREDIT",
                        "timestamp": "2022-05-11T20:00:17.386492513Z"
                    }
                ]""";

        List<Transaction> response = List.of(
                new Transaction(9082348L, 123L, 0.85, "DEBIT", "2022-05-11T23:28:06.492311994Z"),
                new Transaction(65646456L, 123L, 2.0, "CREDIT", "2022-05-11T20:00:17.386492513Z")
        );

        when(walletService.playerHistory(123L)).thenReturn(response);

        var request = MockMvcRequestBuilders.get("/api/wallet/history?playerId=123");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).playerHistory(123L);

    }

    @Test
    void playerHistoryTest_Failure_InvalidPlayerId() throws Exception {

        String expectedResponse = """
                {
                    "message": "Player does not exist!",
                    "statusCode": "500"
                }""";

        doThrow(new AccountServiceException("Player does not exist!")).when(walletService).playerHistory(123L);

        var request = MockMvcRequestBuilders.get("/api/wallet/history?playerId=123");

        mockMvc.perform(request)
                .andExpect(status().is5xxServerError())
                .andExpect(content().json(expectedResponse));

        verify(walletService, times(1)).playerHistory(123L);

    }

}
