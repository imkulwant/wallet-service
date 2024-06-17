package com.kulsin.wallet.converter;

import com.kulsin.wallet.core.transaction.model.TransactionResponse;
import com.kulsin.wallet.model.response.WalletResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TxnResponseToWalletResponse implements Converter<TransactionResponse, WalletResponse> {

    @Override
    public WalletResponse convert(TransactionResponse response) {

        return WalletResponse.builder()
                .playerId(response.getPlayerId())
                .balance(response.getBalance())
                .currency(response.getCurrency())
                .transactionId(response.getTransactionId())
                .build();

    }

}