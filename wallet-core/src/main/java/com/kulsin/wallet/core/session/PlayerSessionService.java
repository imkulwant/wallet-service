package com.kulsin.wallet.core.session;

import com.kulsin.wallet.core.session.entity.PlayerSession;
import com.kulsin.wallet.core.session.repository.PlayerSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerSessionService {

    private final PlayerSessionRepository playerSessionRepository;

    public PlayerSession createSession(Long playerId) {
        PlayerSession playerSession = new PlayerSession();
        playerSession.setPlayerId(playerId);
        playerSession.setSessionToken(UUID.randomUUID().toString());
        playerSession.setStartTime(LocalDateTime.now());
        playerSession.setExpiryTime(LocalDateTime.now().plusMinutes(30));
        return playerSessionRepository.save(playerSession);
    }

    public PlayerSession getPlayerSession(Long playerId) {
        return playerSessionRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public void validateIfSessionIsActive(String sessionToken) {

        PlayerSession playerSession = playerSessionRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (playerSession.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Session expired");
        }
    }

}
