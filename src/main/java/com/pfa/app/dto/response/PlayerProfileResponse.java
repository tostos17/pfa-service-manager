package com.pfa.app.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerProfileResponse {
    private String playerId;
    private String playerName;
    private Double heightCm;
    private Double weightKg;
    private String dominantFoot;
    private String position;
    private Integer preferredJerseyNumber;
    private String biography;
    private String photo;
    private String age;
    private String parentPhone;
    private String category;
}