package com.pfa.app.controller;

import com.pfa.app.dto.request.CreateInvoiceRequest;
import com.pfa.app.dto.response.InvoiceResponse;
import com.pfa.app.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/finance/invoices")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping("/player/{playerId}")
    public ResponseEntity<InvoiceResponse> postInvoiceToPlayer(
            @PathVariable String playerId,
            @Valid @RequestBody CreateInvoiceRequest request
    ) {
        InvoiceResponse response = invoiceService.issueInvoiceToPlayer(playerId, request);
        return ResponseEntity.ok(response);
    }
}