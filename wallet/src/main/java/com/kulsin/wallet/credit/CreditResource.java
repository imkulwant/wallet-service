package com.kulsin.wallet.credit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class CreditResource {

    @Autowired
    CreditService creditService;

    @PostMapping(value = "/credit.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public CreditResponse credit(
            @RequestBody CreditRequest creditRequest
    ) {

        // validate token
        return creditService.creditPlayer(creditRequest);

    }

}
