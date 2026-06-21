package com.pfa.app.controller;

import com.pfa.app.dto.response.AccountStatementResponse;
import com.pfa.app.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @GetMapping("/player/{playerId}/statement")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE') or @securityService.isParentOfPlayer(#playerId, authentication.name)")
    public ResponseEntity<AccountStatementResponse> getPlayerStatement(@PathVariable String playerId) {
        return ResponseEntity.ok(billingService.getPlayerStatement(playerId));
    }
}