package com.pfa.app.service.impl;

import com.pfa.app.dto.request.CreateInvoiceRequest;
import com.pfa.app.dto.response.InvoiceResponse;
import com.pfa.app.model.Account;
import com.pfa.app.model.Invoice;
import com.pfa.app.model.Session;
import com.pfa.app.model.Term;
import com.pfa.app.repository.AccountRepository;
import com.pfa.app.repository.InvoiceRepository;
import com.pfa.app.repository.TermRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock private AccountRepository accountRepository;
    @Mock private TermRepository termRepository;
    @Mock private InvoiceRepository invoiceRepository;

    @InjectMocks private InvoiceServiceImpl invoiceService;

    private Account account;
    private Term term;
    private CreateInvoiceRequest request;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .id(1L)
                .accountId("PFA-SMITH-J-2026-A1B2")
                .currentPending(BigDecimal.ZERO)
                .build();

        Session session = Session.builder().name("26/27").build();
        term = Term.builder().id(1L).name("FIRST").session(session).build();

        request = CreateInvoiceRequest.builder()
                .termId(1L)
                .tuitionFee(new BigDecimal("150000.00"))
                .kitFee(new BigDecimal("35000.00"))
                .otherFees(new BigDecimal("5000.00"))
                .build();
    }

    @Test
    void issueInvoiceToPlayer_Success() {
        // Arrange
        Invoice savedInvoice = Invoice.builder()
                .id(100L)
                .account(account)
                .term(term)
                .tuitionFee(request.getTuitionFee())
                .kitFee(request.getKitFee())
                .otherFees(request.getOtherFees())
                .totalAmount(new BigDecimal("190000.00"))
                .createdAt(LocalDateTime.now())
                .build();

        when(accountRepository.findByPlayerPlayerId("player-uuid")).thenReturn(Optional.of(account));
        when(termRepository.findById(1L)).thenReturn(Optional.of(term));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(savedInvoice);

        // Act
        InvoiceResponse response = invoiceService.issueInvoiceToPlayer("player-uuid", request);

        // Assert
        assertNotNull(response);
        assertEquals(new BigDecimal("190000.00"), response.getTotalAmount());
        assertEquals(new BigDecimal("190000.00"), account.getCurrentPending()); // Aggregate balance updated
        assertEquals("26/27 - FIRST TERM", response.getTermName());

        verify(invoiceRepository, times(1)).save(any(Invoice.class));
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void issueInvoiceToPlayer_AccountNotFound_ThrowsException() {
        // Arrange
        when(accountRepository.findByPlayerPlayerId("invalid-id")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                invoiceService.issueInvoiceToPlayer("invalid-id", request)
        );
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    void issueInvoiceToPlayer_TermNotFound_ThrowsException() {
        // Arrange
        when(accountRepository.findByPlayerPlayerId("player-uuid")).thenReturn(Optional.of(account));
        when(termRepository.findById(1L)).thenReturn(Optional.empty()); // Simulate missing term

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                invoiceService.issueInvoiceToPlayer("player-uuid", request)
        );

        // Verify that we never tried to save an invoice because it failed fast
        verify(invoiceRepository, never()).save(any());
    }
}