package com.pfa.app.service.impl;

import com.pfa.app.dto.request.CreateInvoiceRequest;
import com.pfa.app.dto.response.InvoiceResponse;
import com.pfa.app.model.Account;
import com.pfa.app.model.Invoice;
import com.pfa.app.model.Term;
import com.pfa.app.repository.AccountRepository;
import com.pfa.app.repository.InvoiceRepository;
import com.pfa.app.repository.TermRepository;
import com.pfa.app.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final AccountRepository accountRepository;
    private final TermRepository termRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    @Transactional
    public InvoiceResponse issueInvoiceToPlayer(String playerId, CreateInvoiceRequest request) {
        // 1. Fetch the player's ledger account
        Account account = accountRepository.findByPlayerPlayerId(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice issuance failed: Financial account not found for player ID: " + playerId));

        // 2. Fetch the concrete targeted Term timeline entity
        Term term = termRepository.findById(request.getTermId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice issuance failed: Term not found with ID: " + request.getTermId()));

        // 3. Calculate total billing sum using high-precision math
        BigDecimal totalBill = request.getTuitionFee()
                .add(request.getKitFee())
                .add(request.getOtherFees());

        // 4. Construct and save the Invoice record
        Invoice invoice = Invoice.builder()
                .account(account)
                .term(term)
                .tuitionFee(request.getTuitionFee())
                .kitFee(request.getKitFee())
                .otherFees(request.getOtherFees())
                .totalAmount(totalBill)
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);

        // 5. Update the Account's pending structural balance context
        BigDecimal updatedPending = account.getCurrentPending().add(totalBill);
        account.setCurrentPending(updatedPending);
        accountRepository.save(account);

        // 6. Map and return the structured response DTO
        return InvoiceResponse.builder()
                .id(savedInvoice.getId())
                .termName(term.getSession().getName() + " - " + term.getName() + " TERM")
                .tuitionFee(savedInvoice.getTuitionFee())
                .kitFee(savedInvoice.getKitFee())
                .otherFees(savedInvoice.getOtherFees())
                .totalAmount(savedInvoice.getTotalAmount())
                .createdAt(savedInvoice.getCreatedAt())
                .build();
    }
}