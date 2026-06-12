package com.pfa.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerProfileRequest {

    @NotNull(message = "Height is required")
    @Min(value = 0, message = "Height must be positive")
    private Double heightCm;

    @NotNull(message = "Weight is required")
    @Min(value = 0, message = "Weight must be positive")
    private Double weightKg;

    @NotBlank(message = "Dominant foot is required")
    private String dominantFoot;

    @NotBlank(message = "Position is required")
    private String position;

    private Integer preferredJerseyNumber;
    private String biography;
}