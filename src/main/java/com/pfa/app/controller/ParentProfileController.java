package com.pfa.app.controller;

import com.pfa.app.dto.response.PlayerDataResponse;
import com.pfa.app.service.ParentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parents")
@RequiredArgsConstructor
public class ParentProfileController {

    private final ParentProfileService parentProfileService;

    @GetMapping("/my-roster")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<PlayerDataResponse>> getMyChildrenRoster(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // userDetails.getUsername() reliably extracts the username directly out of the parsed JWT token payload
        List<PlayerDataResponse> roster = parentProfileService.getChildrenRoster(userDetails.getUsername());
        return ResponseEntity.ok(roster);
    }
}