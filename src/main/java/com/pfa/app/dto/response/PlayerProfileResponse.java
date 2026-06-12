package com.pfa.app.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerProfileResponse {
    private String playerId; // Public business UUID string
    private String playerName; // Combined first and last name for convenience
    private Double heightCm;
    private Double weightKg;
    private String dominantFoot;
    private String position;
    private Integer preferredJerseyNumber;
    private String biography;
}