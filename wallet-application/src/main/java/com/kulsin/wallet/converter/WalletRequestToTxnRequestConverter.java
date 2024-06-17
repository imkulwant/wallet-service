package com.kulsin.wallet.converter;

import com.kulsin.wallet.core.transaction.model.TransactionRequest;
import com.kulsin.wallet.model.request.WalletRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WalletRequestToTxnRequestConverter implements Converter<WalletRequest, TransactionRequest> {

    @Override
    public TransactionRequest convert(WalletRequest walletRequest) {

        return TransactionRequest.builder()
                .amount(walletRequest.getAmount())
                .playerId(walletRequest.getPlayerId())
                .transactionId(walletRequest.getTransactionId())
                .type(walletRequest.getType())
                .build();

    }

}