package com.kulsin.wallet.core.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerSessionRepository extends JpaRepository<PlayerSession, Long> {

    Optional<PlayerSession> findByPlayerId(Long playerId);

    Optional<PlayerSession> findBySessionToken(String sessionToken);

    boolean existsBySessionToken(String sessionToken);

}
