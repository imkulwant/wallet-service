package com.kulsin.wallet.debit;

import org.springframework.stereotype.Service;

@Service
public class DebitService {

    public DebitResponse debitPlayer(DebitRequest debitRequest) {
        return new DebitResponse(debitRequest.getPlayerId(),
                54L,
                debitRequest.getTransactionId(),
                "200OK");
    }

}
