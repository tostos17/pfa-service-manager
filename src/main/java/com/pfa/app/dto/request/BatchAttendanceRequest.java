package com.pfa.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchAttendanceRequest {
    @NotNull(message = "Event date is required")
    private LocalDate eventDate;

    @NotBlank(message = "Event type is required")
    private String eventType; // e.g., TRAINING

    private String description;

    @NotEmpty(message = "Attendance logs cannot be empty")
    private List<PlayerAttendanceSubmitDTO> records;
}