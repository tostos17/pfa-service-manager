package com.pfa.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerAttendanceSubmitDTO {
    @NotBlank(message = "Player UUID is required")
    private String playerId; // Business UUID string

    @NotBlank(message = "Attendance status is required")
    private String status;   // PRESENT, ABSENT, etc.

    private String remarks;
}