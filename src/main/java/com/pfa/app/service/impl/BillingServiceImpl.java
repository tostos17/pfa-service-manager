package com.pfa.app.service.impl;

import com.pfa.app.dto.response.AccountStatementResponse;
import com.pfa.app.dto.response.InvoiceResponse;
import com.pfa.app.dto.response.PaymentResponse;
import com.pfa.app.model.Account;
import com.pfa.app.repository.AccountRepository;
import com.pfa.app.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public AccountStatementResponse getPlayerStatement(String playerId) {
        Account account = accountRepository.findByPlayerPlayerId(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Billing statement not found for player ID: " + playerId));

        // Map invoice sub-tree
        List<InvoiceResponse> invoiceDtos = account.getInvoices().stream()
                .map(inv -> InvoiceResponse.builder()
                        .id(inv.getId())
                        .termName(inv.getInvoiceTermDisplayName()) // Ready for Term entity reference swap later
                        .tuitionFee(inv.getTuitionFee())
                        .kitFee(inv.getKitFee())
                        .otherFees(inv.getOtherFees())
                        .totalAmount(inv.getTotalAmount())
                        .createdAt(inv.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // Map payment sub-tree
        List<PaymentResponse> paymentDtos = account.getPayments().stream()
                .map(pmt -> PaymentResponse.builder()
                        .id(pmt.getId())
                        .amountPaid(pmt.getAmountPaid())
                        .paymentMethod(pmt.getPaymentMethod())
                        .referenceNumber(pmt.getReferenceNumber())
                        .paymentDate(pmt.getPaymentDate())
                        .build())
                .collect(Collectors.toList());

        return AccountStatementResponse.builder()
                .accountId(account.getAccountId())
                .playerName(account.getPlayer().getFirstName() + " " + account.getPlayer().getLastName())
                .status(account.isStatus())
                .currentPending(account.getCurrentPending())
                .outstandings(account.getOutstandings())
                .invoices(invoiceDtos)
                .payments(paymentDtos)
                .build();
    }
}