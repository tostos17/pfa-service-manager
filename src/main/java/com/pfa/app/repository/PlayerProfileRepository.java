package com.pfa.app.repository;

import com.pfa.app.model.PlayerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlayerProfileRepository extends JpaRepository<PlayerProfile, Long> {
    // Traverse relationship: PlayerProfile -> PlayerData -> PlayerId (UUID string)
    Optional<PlayerProfile> findByPlayerPlayerId(String playerId);
    Page<PlayerProfile> findAll(Specification<PlayerProfile> spec, Pageable pageable);
}