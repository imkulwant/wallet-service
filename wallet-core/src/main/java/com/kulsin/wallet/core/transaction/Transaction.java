package com.kulsin.wallet.core.transaction;

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
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "transactionId")
    private long transactionId;

    @Column(name = "playerId")
    private long playerId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "transactionType")
    private String transactionType;

    @Column(name = "timestamp")
    private String timestamp;


}
