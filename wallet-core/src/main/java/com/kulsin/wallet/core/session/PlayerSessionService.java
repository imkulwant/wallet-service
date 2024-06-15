package com.kulsin.wallet.core.session;

import com.kulsin.wallet.core.session.entity.PlayerSession;

public interface PlayerSessionService {

    PlayerSession createSession(Long playerId);

    void validateIfSessionIsActive(String sessionToken);

}
