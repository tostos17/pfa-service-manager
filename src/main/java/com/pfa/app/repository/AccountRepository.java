package com.pfa.app.repository;

import com.pfa.app.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    // Lookup by the custom name-reflective human-readable ID
    Optional<Account> findByAccountId(String accountId);

    // Lookup via the core PlayerData business UUID string
    Optional<Account> findByPlayerPlayerId(String playerId);
}