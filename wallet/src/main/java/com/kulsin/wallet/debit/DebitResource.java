package com.kulsin.wallet.debit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebitResource {

    @Autowired
    DebitService debitService;

    @PostMapping(value = "/debit.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public DebitResponse debit(
            @RequestBody DebitRequest debitRequest
    ) {

        // validate token
        return debitService.debitPlayer(debitRequest);

    }

}
