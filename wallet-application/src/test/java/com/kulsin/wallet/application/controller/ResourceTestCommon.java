package com.kulsin.wallet.application.controller;

import com.kulsin.wallet.errorhandling.WalletExceptionHandler;
import com.kulsin.wallet.model.WalletRequest;
import com.kulsin.wallet.model.WalletResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.FixedContentNegotiationStrategy;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Base64;
import java.util.UUID;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ResourceTestCommon {

    public static MockMvc makeMockMvc(Object... resources) {

        ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager(
                new FixedContentNegotiationStrategy(MediaType.APPLICATION_JSON));
        MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
        mappingJackson2JsonView.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return standaloneSetup(resources)
                .setContentNegotiationManager(contentNegotiationManager)
                .setSingleView(mappingJackson2JsonView)
                .setHandlerExceptionResolvers(new WalletExceptionHandler())
                .build();
    }

    public static String basicAuthHeaderValue() {
        return "Basic " + Base64.getEncoder().encodeToString(("test" + ":" + "test").getBytes());
    }

    public static WalletRequest mockCreditRequest() {
        return WalletRequest.builder()
                .amount(5)
                .currency("EUR")
                .playerId(123L)
                .sessionToken(UUID.randomUUID().toString())
                .transactionId(88888L)
                .build();
    }

    public static WalletResponse mockWalletResponse() {
        return WalletResponse.builder()
                .currency("EUR")
                .playerId(123L)
                .balance(5.0)
                .transactionId(77777L)
                .build();
    }

    public static WalletRequest mockDebitRequest() {
        return WalletRequest.builder()
                .amount(1.0)
                .currency("EUR")
                .playerId(123L)
                .sessionToken(UUID.randomUUID().toString())
                .transactionId(99999L)
                .build();
    }

}
