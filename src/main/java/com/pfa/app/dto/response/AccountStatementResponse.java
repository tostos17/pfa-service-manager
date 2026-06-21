package com.pfa.app.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountStatementResponse {
    private String accountId;
    private String playerName;
    private boolean status;
    private BigDecimal currentPending;
    private BigDecimal outstandings;
    private List<InvoiceResponse> invoices;
    private List<PaymentResponse> payments;
}