package com.kulsin.wallet.core.transaction.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TRANSACTION")
public class Transaction {

    @Id
    @Column(name = "TRANSACTION_ID")
    private long transactionId;

    @Column(name = "PLAYER_ID")
    private long playerId;

    @Column(name = "AMOUNT")
    private double amount;

    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;

    @Column(name = "SESSION_TOKEN")
    private String sessionToken;

    @Column(name = "TIMESTAMP")
    private String timestamp;


}
