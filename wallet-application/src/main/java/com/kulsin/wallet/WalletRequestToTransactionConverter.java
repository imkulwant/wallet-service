package com.kulsin.wallet;

import com.kulsin.wallet.core.transaction.Transaction;
import com.kulsin.wallet.model.WalletRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class WalletRequestToTransactionConverter implements Converter<WalletRequest, Transaction> {

    @Override
    public Transaction convert(WalletRequest walletRequest) {

        return Transaction.builder()
                .amount(walletRequest.getAmount())
                .playerId(walletRequest.getPlayerId())
                .timestamp(Instant.now().toString())
                .transactionId(walletRequest.getTransactionId())
                .build();

    }

}
