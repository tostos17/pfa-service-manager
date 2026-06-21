package com.pfa.app.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInvoiceRequest {

    @NotNull(message = "Term ID is required")
    private Long termId;

    @NotNull(message = "Tuition fee cannot be null")
    @PositiveOrZero(message = "Tuition fee must be zero or positive")
    private BigDecimal tuitionFee;

    @NotNull(message = "Kit fee cannot be null")
    @PositiveOrZero(message = "Kit fee must be zero or positive")
    private BigDecimal kitFee;

    @NotNull(message = "Other fees cannot be null")
    @PositiveOrZero(message = "Other fees must be zero or positive")
    private BigDecimal otherFees;
}