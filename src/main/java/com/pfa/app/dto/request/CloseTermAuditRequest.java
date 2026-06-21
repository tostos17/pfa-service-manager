package com.pfa.app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloseTermAuditRequest {
    @NotNull(message = "Term ID to close is required")
    private Long termId;
}