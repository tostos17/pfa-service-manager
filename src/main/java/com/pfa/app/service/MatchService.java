package com.pfa.app.service;

import com.pfa.app.model.Match;
import com.pfa.app.model.MatchEvent;
import com.pfa.app.model.Goal;
import com.pfa.app.model.Substitution;

import java.util.List;

public interface MatchService {
    Match createMatch(Match match);
    Match getMatchById(Long id);
    List<Match> getAllMatches();
    List<Match> getLiveMatches();

    // Core state controls
    Match startMatch(Long matchId);
    Match endMatch(Long matchId);

    // Lineup configuration
    Match updateStartingLineup(Long matchId, List<Long> playerIds);

    // In-game dynamic event additions
    void recordGoal(Long matchId, Goal goal, Long scorerPlayerId);
    void recordSubstitution(Long matchId, Substitution substitution, Long playerInId, Long playerOutId);
    void recordGenericEvent(Long matchId, MatchEvent event);
}