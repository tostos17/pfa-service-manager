package com.pfa.app.controller;

import com.pfa.app.service.PlayerDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerDataController {

    private final PlayerDataService playerDataService;

    @PutMapping(value = "/{playerId}/passport", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ResponseEntity<String> updatePassportPhoto(
            @PathVariable String playerId,
            @RequestParam("username") String username, // Passed to verify security context matches target user
            @RequestParam("passportPhoto") MultipartFile passportPhoto
    ) {
        String updatedUrl = playerDataService.updatePlayerPassport(playerId, passportPhoto);
        return ResponseEntity.ok(updatedUrl);
    }
}