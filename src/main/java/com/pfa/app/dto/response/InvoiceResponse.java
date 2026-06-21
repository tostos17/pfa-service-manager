package com.pfa.app.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {
    private Long id;
    private String termName; // Will easily map from a Term entity relation later
    private BigDecimal tuitionFee;
    private BigDecimal kitFee;
    private BigDecimal otherFees;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}