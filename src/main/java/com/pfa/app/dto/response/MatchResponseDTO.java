package com.pfa.app.dto.response;

import com.pfa.app.model.MatchType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record MatchResponseDTO(
        Long id,
        String categoryName,
        String opponentName,
        LocalDate date,
        LocalTime time,
        String venue,
        boolean started,
        boolean isLive,
        boolean ended,
        boolean isHomeMatch,
        boolean isAwayMatch,
        int homeTeamScore,
        int awayTeamScore,
        MatchType type,
        int halfDuration,
        List<String> photoUrls,
        List<String> videoUrls,
        List<Long> startingLineupPlayerIds // Simplifies tracking links by passing IDs instead of nested entity trees
) {}

