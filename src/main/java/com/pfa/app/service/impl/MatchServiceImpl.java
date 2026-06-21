package com.pfa.app.service.impl;

import com.pfa.app.exception.ResourceNotFoundException;
import com.pfa.app.model.*;
import com.pfa.app.repository.*;
import com.pfa.app.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository; // Used to fetch lineup/event players
    private final MatchEventRepository matchEventRepository;

    @Override
    @Transactional
    public Match createMatch(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public Match getMatchById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match data records not found for ID: " + id));
    }

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public List<Match> getLiveMatches() {
        return matchRepository.findByIsLiveTrue();
    }

    @Override
    @Transactional
    public Match startMatch(Long matchId) {
        Match match = getMatchById(matchId);
        match.setStarted(true);
        match.setLive(true);
        return matchRepository.save(match);
    }

    @Override
    @Transactional
    public Match updateStartingLineup(Long matchId, List<Long> playerIds) {
        Match match = getMatchById(matchId);
        List<Player> selectedPlayers = playerRepository.findAllById(playerIds);

        match.setStartingLineup(selectedPlayers);
        return matchRepository.save(match);
    }

    @Override
    @Transactional
    public void recordGoal(Long matchId, Goal goal, Long scorerPlayerId) {
        Match match = getMatchById(matchId);
        Player scorer = playerRepository.findById(scorerPlayerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with ID: " + scorerPlayerId));

        goal.setMatch(match);
        goal.setScorer(scorer);
        match.getGoalsScored().add(goal);

        // Dynamically increment score based on home/away layout variables
        if (match.isHomeMatch()) {
            if (!goal.isOwnGoal()) {
                match.setHomeTeamScore(match.getHomeTeamScore() + 1);
            } else {
                match.setAwayTeamScore(match.getAwayTeamScore() + 1);
            }
        } else { // Away Match layout configurations
            if (!goal.isOwnGoal()) {
                match.setAwayTeamScore(match.getAwayTeamScore() + 1);
            } else {
                match.setHomeTeamScore(match.getHomeTeamScore() + 1);
            }
        }

        matchRepository.save(match);
    }

    @Override
    @Transactional
    public void recordSubstitution(Long matchId, Substitution sub, Long playerInId, Long playerOutId) {
        Match match = getMatchById(matchId);
        Player playerIn = playerRepository.findById(playerInId)
                .orElseThrow(() -> new ResourceNotFoundException("Incoming player data not found: " + playerInId));
        Player playerOut = playerRepository.findById(playerOutId)
                .orElseThrow(() -> new ResourceNotFoundException("Outgoing player data not found: " + playerOutId));

        sub.setMatch(match);
        sub.setPlayerIn(playerIn);
        sub.setPlayerOut(playerOut);
        match.getSubstitutions().add(sub);

        matchRepository.save(match);
    }

    @Override
    @Transactional
    public void recordGenericEvent(Long matchId, MatchEvent event) {
        Match match = getMatchById(matchId);
        event.setMatch(match);

        // Polymorphic strategy writes parent match links & fields via JPA cascade hooks
        matchEventRepository.save(event);
    }

    @Override
    @Transactional
    public Match endMatch(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        // If it's a competitive knockout match and the scores are level, determine progression
        if (match.getType() == MatchType.COMPETITIVE && match.getHomeTeamScore() == match.getAwayTeamScore()) {

            // 1. If currently in normal time, transition into Extra Time (2 halves)
            if (!match.isExtraTime() && !match.isPenaltyShootout()) {
                match.setExtraTime(true);

                MatchStateEvent etEvent = new MatchStateEvent();
                etEvent.setMatch(match);
                etEvent.setMatchTime("90");
                etEvent.setStateName("EXTRA_TIME_STARTED");
                matchEventRepository.save(etEvent);

                return matchRepository.save(match); // Remains live
            }

            // 2. If already played extra time and still tied, head to penalties
            if (match.isExtraTime() && !match.isPenaltyShootout()) {
                match.setExtraTime(false);
                match.setPenaltyShootout(true);

                MatchStateEvent penEvent = new MatchStateEvent();
                penEvent.setMatch(match);
                penEvent.setMatchTime("120");
                penEvent.setStateName("PENALTY_SHOOTOUT_STARTED");
                matchEventRepository.save(penEvent);

                return matchRepository.save(match); // Remains live
            }
        }

        // Default or broken tie conclusion behavior: wrap up game metrics cleanly
        match.setLive(false);
        match.setEnded(true);
        match.setExtraTime(false);
        match.setPenaltyShootout(false);

        MatchStateEvent endEvent = new MatchStateEvent();
        endEvent.setMatch(match);
        endEvent.setMatchTime(match.isPenaltyShootout() ? "PEN" : "FT");
        endEvent.setStateName("MATCH_CONCLUDED");
        matchEventRepository.save(endEvent);

        return matchRepository.save(match);
    }

    @Transactional
    public Match recordPenaltyScore(Long matchId, int homePenalties, int awayPenalties) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        if (!match.isPenaltyShootout()) {
            throw new IllegalStateException("Cannot log shootout tallies unless match status is explicitly set to penalty shootout.");
        }

        match.setHomeTeamPenaltyScore(homePenalties);
        match.setAwayTeamPenaltyScore(awayPenalties);
        return matchRepository.save(match);
    }
}