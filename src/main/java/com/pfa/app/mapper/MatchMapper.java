package com.pfa.app.mapper;

import com.pfa.app.dto.request.MatchRequestDTO;
import com.pfa.app.dto.response.MatchResponseDTO;
import com.pfa.app.model.Match;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MatchMapper {

    /**
     * Converts a MatchRequestDTO into a fresh Match Entity domain object.
     * Perfect for creation endpoints.
     */
    public Match toEntity(MatchRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Match.builder()
                .opponentName(dto.opponentName())
                .date(dto.date())
                .time(dto.time())
                .venue(dto.venue())
                .isHomeMatch(dto.isHomeMatch())
                .isAwayMatch(dto.isAwayMatch())
                .type(dto.type())
                .halfDuration(dto.halfDuration())
                // Set default baseline operational states for new matches
                .started(false)
                .isLive(false)
                .ended(false)
                .homeTeamScore(0)
                .awayTeamScore(0)
                .photoUrls(new ArrayList<>())
                .videoUrls(new ArrayList<>())
                .startingLineup(new ArrayList<>())
                .build();
    }

    /**
     * Converts a Match Entity into an immutable, flat MatchResponseDTO payload.
     * Prevents infinite serialization loops when fetching nested player relations.
     */
    public MatchResponseDTO toResponseDTO(Match match) {
        if (match == null) {
            return null;
        }

        // Safely extract primitive ID keys from the active squad entities array
        List<Long> startingLineupPlayerIds = match.getStartingLineup() != null
                ? match.getStartingLineup().stream().map(player -> player.getId()).toList()
                : List.of();

        String categoryname = match.getCategory() != null ? match.getCategory().getName() : "GENERAL";

        return new MatchResponseDTO(
                match.getId(),
                categoryname,
                match.getOpponentName(),
                match.getDate(),
                match.getTime(),
                match.getVenue(),
                match.isStarted(),
                match.isLive(),
                match.isEnded(),
                match.isHomeMatch(),
                match.isAwayMatch(),
                match.getHomeTeamScore(),
                match.getAwayTeamScore(),
                match.getType(),
                match.getHalfDuration(),
                match.getPhotoUrls() != null ? List.copyOf(match.getPhotoUrls()) : List.of(),
                match.getVideoUrls() != null ? List.copyOf(match.getVideoUrls()) : List.of(),
                startingLineupPlayerIds
        );
    }

    /**
     * Updates an existing Match Entity with incoming structural modifications from a request DTO.
     * Great for put/patch update endpoints.
     */
    public void updateEntityFromDTO(MatchRequestDTO dto, Match match) {
        if (dto == null || match == null) {
            return;
        }

        match.setOpponentName(dto.opponentName());
        match.setDate(dto.date());
        match.setTime(dto.time());
        match.setVenue(dto.venue());
        match.setHomeMatch(dto.isHomeMatch());
        match.setAwayMatch(dto.isAwayMatch());
        match.setType(dto.type());
        match.setHalfDuration(dto.halfDuration());
    }
}