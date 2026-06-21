package com.pfa.app.repository;

import com.pfa.app.model.MatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {
    // Fetch timeline events sequentially for a given match
    List<MatchEvent> findByMatchIdOrderByMatchTimeAsc(Long matchId);
}