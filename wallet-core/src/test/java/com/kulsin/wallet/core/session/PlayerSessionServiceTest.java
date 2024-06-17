package com.kulsin.wallet.core.session;

import com.kulsin.wallet.core.session.entity.PlayerSession;
import com.kulsin.wallet.core.session.repository.PlayerSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlayerSessionServiceTest {

    @Mock
    private PlayerSessionRepository playerSessionRepository;

    @InjectMocks
    private PlayerSessionServiceImpl playerSessionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSession() {
        Long playerId = 1L;
        PlayerSession playerSession = new PlayerSession();
        playerSession.setPlayerId(playerId);
        playerSession.setSessionToken(UUID.randomUUID().toString());
        playerSession.setStartTime(LocalDateTime.now());
        playerSession.setExpiryTime(LocalDateTime.now().plusMinutes(30));

        when(playerSessionRepository.save(any(PlayerSession.class))).thenReturn(playerSession);

        PlayerSession createdSession = playerSessionService.createSession(playerId);

        assertEquals(playerId, createdSession.getPlayerId());
        assertNotNull(createdSession.getSessionToken());
        assertNotNull(createdSession.getStartTime());
        assertNotNull(createdSession.getExpiryTime());
        verify(playerSessionRepository, times(1)).save(any(PlayerSession.class));
    }

    @Test
    public void testValidateIfSessionIsActive() {
        String sessionToken = UUID.randomUUID().toString();
        PlayerSession playerSession = new PlayerSession();
        playerSession.setSessionToken(sessionToken);
        playerSession.setExpiryTime(LocalDateTime.now().plusMinutes(30));

        when(playerSessionRepository.findBySessionToken(sessionToken)).thenReturn(Optional.of(playerSession));

        assertDoesNotThrow(() -> playerSessionService.validateIfSessionIsActive(sessionToken));
        verify(playerSessionRepository, times(1)).findBySessionToken(sessionToken);
    }

    @Test
    public void testValidateIfSessionIsActiveThrowsExceptionForNonexistentSession() {
        String sessionToken = UUID.randomUUID().toString();

        when(playerSessionRepository.findBySessionToken(sessionToken)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> playerSessionService.validateIfSessionIsActive(sessionToken));

        assertEquals("Session not found", exception.getMessage());
        verify(playerSessionRepository, times(1)).findBySessionToken(sessionToken);
    }

    @Test
    public void testValidateIfSessionIsActiveThrowsExceptionForExpiredSession() {
        String sessionToken = UUID.randomUUID().toString();
        PlayerSession playerSession = new PlayerSession();
        playerSession.setSessionToken(sessionToken);
        playerSession.setExpiryTime(LocalDateTime.now().minusMinutes(1));

        when(playerSessionRepository.findBySessionToken(sessionToken)).thenReturn(Optional.of(playerSession));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> playerSessionService.validateIfSessionIsActive(sessionToken));

        assertEquals("Session expired", exception.getMessage());
        verify(playerSessionRepository, times(1)).findBySessionToken(sessionToken);
    }

}