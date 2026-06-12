package com.pfa.app.repository;

import com.pfa.app.model.PlayerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlayerProfileRepository extends JpaRepository<PlayerProfile, Long> {
    // Traverse relationship: PlayerProfile -> PlayerData -> PlayerId (UUID string)
    Optional<PlayerProfile> findByPlayerDataPlayerId(String playerId);
}