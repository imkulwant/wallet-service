package com.kulsin.wallet.core.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ACCOUNT")
public class Account {

    @Id
    @Column(name = "PLAYER_ID", nullable = false, unique = true)
    private long playerId;

    @Column(name = "BALANCE", nullable = false)
    private double balance;

    @Column(name = "CURRENCY", nullable = false)
    private String currency;

}
