package com.kulsin.wallet.application.controller.balance;

import com.kulsin.wallet.application.errorhandling.WalletExceptionHandler;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.FixedContentNegotiationStrategy;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

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

}
