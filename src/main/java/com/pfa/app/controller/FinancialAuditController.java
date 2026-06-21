package com.pfa.app.controller;

import com.pfa.app.dto.request.CloseTermAuditRequest;
import com.pfa.app.service.FinancialAuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/audit")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class FinancialAuditController {

    private final FinancialAuditService auditService;

    @PostMapping("/term-close")
    public ResponseEntity<String> closeTermAndRolloverBalances(@Valid @RequestBody CloseTermAuditRequest request) {
        auditService.executeTermRolloverAudit(request);
        return ResponseEntity.ok("Term audit executed successfully. Unpaid balances have been rolled over into historical outstandings.");
    }
}