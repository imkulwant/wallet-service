package com.kulsin.wallet.core.session.repository;

import com.kulsin.wallet.core.session.entity.PlayerSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerSessionRepository extends JpaRepository<PlayerSession, Long> {

    Optional<PlayerSession> findBySessionToken(String sessionToken);

}
