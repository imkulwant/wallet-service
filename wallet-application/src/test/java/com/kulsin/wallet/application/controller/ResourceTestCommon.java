package com.kulsin.wallet.application.controller;

import com.kulsin.wallet.core.transaction.model.TransactionResponse;
import com.kulsin.wallet.exception.WalletExceptionHandler;
import com.kulsin.wallet.model.request.WalletRequest;
import com.kulsin.wallet.model.response.WalletResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.FixedContentNegotiationStrategy;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Base64;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ResourceTestCommon {

    public static final Long PLAYER_ID = 123L;

    public static MockMvc makeMockMvc(Object... resources) {

        ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager(
                new FixedContentNegotiationStrategy(MediaType.APPLICATION_JSON));
        MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
        mappingJackson2JsonView.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return standaloneSetup(resources)
                .setContentNegotiationManager(contentNegotiationManager)
                .setControllerAdvice(WalletExceptionHandler.class)
                .setSingleView(mappingJackson2JsonView)
                .build();
    }

    public static String basicAuthHeaderValue() {
        return "Basic " + Base64.getEncoder().encodeToString(("test" + ":" + "test").getBytes());
    }

    public static WalletRequest mockCreditRequest() {
        return WalletRequest.builder()
                .amount(5.00)
                .type("credit")
                .currency("EUR")
                .playerId(123L)
                .sessionToken("test-token")
                .transactionId(989898L)
                .build();
    }

    public static WalletResponse mockWalletResponse() {
        return WalletResponse.builder()
                .currency("EUR")
                .playerId(123L)
                .balance(5.00)
                .transactionId(989898L)
                .build();
    }

    public static WalletRequest mockDebitRequest() {
        return WalletRequest.builder()
                .amount(5.0)
                .type("debit")
                .currency("EUR")
                .playerId(123L)
                .sessionToken("test-token")
                .transactionId(989898L)
                .build();
    }

    public static TransactionResponse mockTransactionResponse() {
        return TransactionResponse.builder()
                .playerId(PLAYER_ID)
                .balance(100.0)
                .transactionId(989898L)
                .build();

    }
}
