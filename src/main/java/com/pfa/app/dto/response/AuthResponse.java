package com.pfa.app.dto.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private boolean requirePasswordChange;
    private String fullName;
    private Set<String> roles;
    private String profilePictureUrl;
}