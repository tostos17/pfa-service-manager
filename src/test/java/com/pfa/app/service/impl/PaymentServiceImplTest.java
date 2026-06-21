package com.pfa.app.service.impl;

import com.pfa.app.dto.request.ProcessPaymentRequest;
import com.pfa.app.dto.response.PaymentResponse;
import com.pfa.app.model.Account;
import com.pfa.app.model.Payment;
import com.pfa.app.repository.AccountRepository;
import com.pfa.app.repository.PaymentRepository;
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
class PaymentServiceImplTest {

    @Mock private AccountRepository accountRepository;
    @Mock private PaymentRepository paymentRepository;

    @InjectMocks private PaymentServiceImpl paymentService;

    @Test
    void processPlayerPayment_Success() {
        // Arrange
        Account account = Account.builder()
                .id(1L)
                .currentPending(new BigDecimal("190000.00"))
                .outstandings(BigDecimal.ZERO)
                .status(false) // Suspended or out of standing due to initial invoice debt
                .build();

        ProcessPaymentRequest request = ProcessPaymentRequest.builder()
                .amountPaid(new BigDecimal("190000.00"))
                .paymentMethod("BANK_TRANSFER")
                .referenceNumber("REF123")
                .build();

        Payment savedPayment = Payment.builder()
                .id(50L)
                .account(account)
                .amountPaid(request.getAmountPaid())
                .paymentMethod("BANK_TRANSFER")
                .referenceNumber("REF123")
                .paymentDate(LocalDateTime.now())
                .build();

        when(accountRepository.findByPlayerPlayerId("player-uuid")).thenReturn(Optional.of(account));
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        // Act
        PaymentResponse response = paymentService.processPlayerPayment("player-uuid", request);

        // Assert
        assertNotNull(response);
        // compareTo returns 0 if the values are mathematically equal
        assertEquals(0, BigDecimal.ZERO.compareTo(account.getCurrentPending()),
                "Account pending balance should be mathematically equal to zero");
        assertTrue(account.isStatus()); // Automated standing status toggled back to active

        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(accountRepository, times(1)).save(account);
    }
}