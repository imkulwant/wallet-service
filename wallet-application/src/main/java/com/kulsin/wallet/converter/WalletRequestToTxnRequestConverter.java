package com.kulsin.wallet.converter;

import com.kulsin.wallet.core.transaction.model.TransactionRequest;
import com.kulsin.wallet.model.request.CreditRequest;
import com.kulsin.wallet.model.request.DebitRequest;
import com.kulsin.wallet.model.request.WalletRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WalletRequestToTxnRequestConverter implements Converter<WalletRequest, TransactionRequest> {

    @Override
    public TransactionRequest convert(WalletRequest walletRequest) {


        if (walletRequest instanceof CreditRequest) {

            return convert((CreditRequest) walletRequest);

        } else if (walletRequest instanceof DebitRequest) {

            return convert((DebitRequest) walletRequest);

        }

        throw new RuntimeException("Unsupported wallet request type");

    }

    private TransactionRequest convert(CreditRequest creditRequest) {

        return TransactionRequest.builder()
                .amount(creditRequest.getAmount())
                .playerId(creditRequest.getPlayerId())
                .transactionId(creditRequest.getTransactionId())
                .type("CREDIT")
                .build();

    }

    private TransactionRequest convert(DebitRequest creditRequest) {

        return TransactionRequest.builder()
                .amount(creditRequest.getAmount())
                .playerId(creditRequest.getPlayerId())
                .transactionId(creditRequest.getTransactionId())
                .type("DEBIT")
                .build();

    }

}
