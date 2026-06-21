package com.pfa.app.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long id;
    private BigDecimal amountPaid;
    private String paymentMethod;
    private String referenceNumber;
    private LocalDateTime paymentDate;
}