package com.kulsin.wallet.core.session;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "PLAYER_SESSION")
public class PlayerSession {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PLAYER_ID", nullable = false)
    private Long playerId;

    @Column(name = "SESSION_TOKEN", nullable = false, unique = true)
    private String sessionToken;

    @Column(name = "START_TIME", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "EXPIRY_TIME", nullable = false)
    private LocalDateTime expiryTime;

}
