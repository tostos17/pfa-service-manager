package com.pfa.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessPaymentRequest {

    @NotNull(message = "Payment amount cannot be null")
    @Positive(message = "Payment amount must be greater than zero")
    private BigDecimal amountPaid;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod; // e.g., "BANK_TRANSFER", "CASH", "CARD"

    private String referenceNumber; // e.g., Bank transaction reference ID
}