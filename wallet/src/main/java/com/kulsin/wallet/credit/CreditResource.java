package com.kulsin.wallet.credit;

import com.kulsin.wallet.common.WalletBaseRequest;
import com.kulsin.wallet.common.WalletBaseResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class CreditResource {

    private final CreditService creditService;

    public CreditResource(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping(value = "/credit.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletBaseResponse credit(
            @RequestBody WalletBaseRequest creditRequest
    ) {

        return creditService.creditPlayer(creditRequest);

    }

}
