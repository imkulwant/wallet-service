package com.kulsin.account.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
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

}
