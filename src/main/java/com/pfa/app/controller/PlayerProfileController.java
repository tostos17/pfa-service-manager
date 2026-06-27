package com.pfa.app.controller;

import com.pfa.app.dto.request.*;
import com.pfa.app.dto.response.ApiResponse;
import com.pfa.app.dto.response.PlayerProfileResponse;
import com.pfa.app.enums.Position;
import com.pfa.app.service.PlayerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH')")
    public ResponseEntity<Page<PlayerProfileResponse>> getPlayerProfiles(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean healthy,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        Page<PlayerProfileResponse> profiles = playerProfileService.getAllPlayerProfiles(search, healthy, pageable);
        return ResponseEntity.ok(profiles);
    }

    @PutMapping("/squadnumber")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Update player's squad number")
    public ApiResponse<String> updateSquadNumber(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody UpdateSquadNumberRequest request
    ) {
        return ApiResponse.ok(playerProfileService.updateSquadNumber(httpServletRequest, request));
    }

    @PutMapping("/height")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Update player's height", description = "Measurement must be in cm")
    public ApiResponse<String> updatePlayerHeight(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody UpdatePlayerWeightHeightRequest request
    ) {
        return ApiResponse.ok(playerProfileService.updatePlayerHeight(httpServletRequest, request));
    }

    @PutMapping("/weight")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Update player's weight", description = "Measurement must be in Kg")
    public ApiResponse<String> updatePlayerWeight(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody UpdatePlayerWeightHeightRequest request
    ) {
        return ApiResponse.ok(playerProfileService.updatePlayerWeight(httpServletRequest, request));
    }

    @PutMapping("/position")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Update player's position")
    public ApiResponse<String> updatePlayerPosition(
            HttpServletRequest httpServletRequest,
            @RequestBody UpdatePlayerPsitionRequest request
    ) {
        return ApiResponse.ok(playerProfileService.updatePlayerPosition(httpServletRequest, request));
    }

    @PutMapping("/category")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Update player's category")
    public ApiResponse<String> updatePlayerCategory(
            HttpServletRequest httpServletRequest,
            @RequestBody UpdatePlayerCategoryRequest request
    ) {
        return ApiResponse.ok(playerProfileService.updatePlayerCategory(httpServletRequest, request));
    }

    @PutMapping("/foot")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Update player's category")
    public ApiResponse<String> updatePlayerDominantFoot(
            HttpServletRequest httpServletRequest,
            @RequestBody UpdatePlayerDominantFootRequest request
    ) {
        return ApiResponse.ok(playerProfileService.updateDominantFoot(httpServletRequest, request));
    }
}