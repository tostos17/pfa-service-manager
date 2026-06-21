package com.pfa.app.controller;

import com.pfa.app.constants.StringValues;
import com.pfa.app.dto.response.ApiResponse;
import com.pfa.app.dto.response.PlayerDataResponse;
import com.pfa.app.model.Player;
import com.pfa.app.service.PlayerDataService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(StringValues.PLAYER_PATH)
@RequiredArgsConstructor
public class PlayerDataController {

    private final PlayerDataService playerDataService;

    @PutMapping(value = "/{playerId}/passport", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    @Operation(summary = "Update player photo", description = "Updates player's passport photograph")
    public ResponseEntity<String> updatePassportPhoto(
            @PathVariable String playerId,
            @RequestParam("username") String username, // Passed to verify security context matches target user
            @RequestParam("passportPhoto") MultipartFile passportPhoto
    ) {
        String updatedUrl = playerDataService.updatePlayerPassport(playerId, passportPhoto);
        return ResponseEntity.ok(updatedUrl);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @Operation(summary = "Get all players")
    public ApiResponse<Page<PlayerDataResponse>> getPlayers(HttpServletRequest httpServletRequest,
                                                            @PageableDefault(page = 0, size = 20, sort = "registrationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return playerDataService.getPlayers(pageable);
    }

}