package com.kulsin.wallet.application.controller.credit;

import com.kulsin.wallet.application.model.WalletRequest;
import com.kulsin.wallet.application.model.WalletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
public class CreditResource {

    private final CreditService creditService;

    public CreditResource(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping(value = "/v1/wallet/credit.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletResponse credit(
            @RequestBody @Valid WalletRequest creditRequest
    ) {

        return creditService.creditPlayer(creditRequest);

    }

}
