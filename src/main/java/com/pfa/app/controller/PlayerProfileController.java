package com.pfa.app.controller;

import com.pfa.app.dto.request.PlayerProfileRequest;
import com.pfa.app.dto.response.PlayerProfileResponse;
import com.pfa.app.service.PlayerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/players/{playerId}/profile")
@RequiredArgsConstructor
public class PlayerProfileController {

    private final PlayerProfileService playerProfileService;

    // Handles both initial creation and downstream updates seamlessly (Idempotent)
    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH') or #username == authentication.name")
    public ResponseEntity<PlayerProfileResponse> saveProfile(
            @PathVariable String playerId,
            @RequestParam("username") String username, // Passed to ensure matching token safety check
            @Valid @RequestBody PlayerProfileRequest request
    ) {
        return ResponseEntity.ok(playerProfileService.createOrUpdateProfile(playerId, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH', 'PARENT', 'PLAYER')")
    public ResponseEntity<PlayerProfileResponse> getProfile(@PathVariable String playerId) {
        return ResponseEntity.ok(playerProfileService.getProfileByPlayerId(playerId));
    }
}