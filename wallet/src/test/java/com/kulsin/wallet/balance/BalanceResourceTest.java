package com.kulsin.wallet.balance;

import com.kulsin.wallet.errorhandling.WalletException;
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

import static com.kulsin.wallet.balance.ResourceTestCommon.makeMockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BalanceResourceTest {

    @Mock
    private BalanceService balanceService;
    @InjectMocks
    private BalanceResource balanceResource;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = makeMockMvc(balanceResource);
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

        Mockito.when(balanceService.playerBalance(123L))
                .thenReturn(new WalletResponse(123L, 5.0, 345678087));

        var request = MockMvcRequestBuilders.post("/v1/wallet/balance.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBalanceRequestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBalanceResponse));

        verify(balanceService, times(1)).playerBalance(123L);

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
                        "errorStatus": 200
                    }
                """;

        var request = MockMvcRequestBuilders.post("/v1/wallet/balance.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBalanceRequestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBalanceResponse));

        verifyNoInteractions(balanceService);

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
                        "errorStatus": 200
                    }
                """;

        Mockito.when(balanceService.playerBalance(123L))
                .thenThrow(new WalletException("Invalid player id! player account doesn't exists"));

        var request = MockMvcRequestBuilders.post("/v1/wallet/balance.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBalanceRequestPayload);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBalanceResponse));

        verify(balanceService, times(1)).playerBalance(123L);

    }

}