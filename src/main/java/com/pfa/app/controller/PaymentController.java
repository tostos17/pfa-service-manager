package com.pfa.app.controller;

import com.pfa.app.dto.request.ProcessPaymentRequest;
import com.pfa.app.dto.response.PaymentResponse;
import com.pfa.app.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/finance/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/player/{playerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE') or @securityService.isParentOfPlayer(#playerId, authentication.name)")
    public ResponseEntity<PaymentResponse> postPayment(
            @PathVariable String playerId,
            @Valid @RequestBody ProcessPaymentRequest request
    ) {
        PaymentResponse response = paymentService.processPlayerPayment(playerId, request);
        return ResponseEntity.ok(response);
    }
}