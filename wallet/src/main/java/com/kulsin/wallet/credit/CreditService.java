package com.kulsin.wallet.credit;

import org.springframework.stereotype.Service;

@Service
public class CreditService {

    public CreditResponse creditPlayer(CreditRequest creditRequest) {
        return new CreditResponse(creditRequest.getPlayerId(),
                54L,
                creditRequest.getTransactionId(),
                "200OK");
    }

}
