package com.pfa.app.dto.response;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDataResponse {
    private String playerId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String parent;
    private boolean healthy;
    private String healthConcernDescription;
    private String passportUrl; // Absolute S3 URL link for rendering on the client
}