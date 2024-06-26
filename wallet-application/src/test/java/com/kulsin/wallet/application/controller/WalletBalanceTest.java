package com.kulsin.wallet.application.controller;

import com.kulsin.wallet.config.HttpBasicAuthConfig;
import com.kulsin.wallet.controller.WalletResource;
import com.kulsin.wallet.core.account.exception.AccountServiceException;
import com.kulsin.wallet.exception.WalletException;
import com.kulsin.wallet.model.request.BalanceRequest;
import com.kulsin.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.kulsin.wallet.application.controller.ResourceTestCommon.basicAuthHeaderValue;
import static com.kulsin.wallet.application.controller.ResourceTestCommon.mockWalletResponse;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(HttpBasicAuthConfig.class)
@WebMvcTest(value = WalletResource.class)
class WalletBalanceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Test
    void getBalanceTest() throws Exception {
        String getBalanceRequestPayload = """
                {
                    "playerId": 123,
                    "amount": 5.00,
                    "currency": "EUR",
                    "sessionToken":"<valid-token-from-authenticate-response>",
                    "transactionId": 989898
                }""";

        String expectedBalanceResponse = """
                {
                    "playerId": 123,
                    "balance": 5.0,
                    "currency": "EUR"
                }""";

        when(walletService.playerBalance(any(BalanceRequest.class))).thenReturn(mockWalletResponse());

        var request = post("/api/wallet/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, basicAuthHeaderValue())
                .content(getBalanceRequestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBalanceResponse));

        verify(walletService, times(1)).playerBalance(any(BalanceRequest.class));

    }

    @Test
    void getBalanceTest_InvalidRequest() throws Exception {
        String getBalanceRequestPayload = """
                {
                    "amount": 5.00,
                    "currency": "EUR",
                    "sessionToken":"test-token",
                    "transactionId": 989898
                }""";

        String expectedBalanceResponse = """
                {
                    "message": "Mandatory field playerId is missing",
                    "statusCode": "400"
                }""";

        var request = post("/api/wallet/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, basicAuthHeaderValue())
                .content(getBalanceRequestPayload);

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(expectedBalanceResponse));

        verifyNoInteractions(walletService);

    }

    @Test
    void getBalanceTest_InvalidPlayerId() throws Exception {
        String getBalanceRequestPayload = """
                {
                    "playerId": 123,
                    "sessionToken":"test-token"
                }""";

        String expectedBalanceResponse = """
                {
                    "message": "Invalid player id! player account doesn't exists",
                    "statusCode": "400"
                }""";

        when(walletService.playerBalance(any(BalanceRequest.class)))
                .thenThrow(new WalletException("Invalid player id! player account doesn't exists"));

        var request = post("/api/wallet/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, basicAuthHeaderValue())
                .content(getBalanceRequestPayload);

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(expectedBalanceResponse));

        verify(walletService, times(1)).playerBalance(any(BalanceRequest.class));

    }

    @Test
    void getBalanceTest_UnExpected_AccountServiceException() throws Exception {
        String getBalanceRequestPayload = """
                {
                    "playerId": 123,
                    "sessionToken":"test-token"
                }""";

        String expectedBalanceResponse = """
                {
                    "message": "Unexpected error occurred while fetching player balance",
                    "statusCode": "500"
                }""";

        Mockito.when(walletService.playerBalance(any(BalanceRequest.class)))
                .thenThrow(new AccountServiceException("Unexpected error occurred while fetching player balance"));

        var request = post("/api/wallet/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, basicAuthHeaderValue())
                .content(getBalanceRequestPayload);

        mockMvc.perform(request)
                .andExpect(status().is5xxServerError())
                .andExpect(content().json(expectedBalanceResponse));

        verify(walletService, times(1)).playerBalance(any(BalanceRequest.class));

    }

}
