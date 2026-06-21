package com.pfa.app.repository;

import com.pfa.app.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    // Find upcoming matches or historical records
    List<Match> findByEnded(boolean ended);

    // Find active games currently being played
    List<Match> findByIsLiveTrue();

    // Query matches containing a specific starting player (Using explicit JPQL join to bypass complex method names)
    @Query("SELECT m FROM Match m JOIN m.startingLineup p WHERE p.playerId = :playerId")
    List<Match> findMatchesByPlayerId(@Param("playerId") String playerId);
}