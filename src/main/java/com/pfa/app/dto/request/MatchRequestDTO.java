package com.pfa.app.dto.request;

import com.pfa.app.model.MatchType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record MatchRequestDTO(
        @NotBlank(message = "Opponent name is required")
        String opponentName,

        @NotNull(message = "Match date is required")
        LocalDate date,

        @NotNull(message = "Match time is required")
        LocalTime time,

        @NotBlank(message = "Venue is required")
        String venue,

        @NotNull(message = "Please specify if this is a home match")
        Boolean isHomeMatch,

        @NotNull(message = "Please specify if this is an away match")
        Boolean isAwayMatch,

        @NotNull(message = "Match type (FRIENDLY/COMPETITIVE) is required")
        MatchType type,

        @Min(value = 1, message = "Half duration must be at least 1 minute")
        int halfDuration,
        
        long categoryid
) {}