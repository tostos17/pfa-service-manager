package com.pfa.app.service.impl;

import com.pfa.app.dto.request.ProcessPaymentRequest;
import com.pfa.app.dto.response.PaymentResponse;
import com.pfa.app.model.Account;
import com.pfa.app.model.Payment;
import com.pfa.app.repository.AccountRepository;
import com.pfa.app.repository.PaymentRepository;
import com.pfa.app.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentResponse processPlayerPayment(String playerId, ProcessPaymentRequest request) {
        // 1. Locate the player's billing ledger account
        Account account = accountRepository.findByPlayerPlayerId(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Payment processing failed: Account not found for player ID: " + playerId));

        // 2. Instantiate and attach the new Payment entity
        Payment payment = Payment.builder()
                .account(account)
                .amountPaid(request.getAmountPaid())
                .paymentMethod(request.getPaymentMethod().toUpperCase())
                .referenceNumber(request.getReferenceNumber())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // 3. Deduct the paid amount from the current pending account balance using high-precision math
        // If amountPaid exceeds currentPending, the balance can go negative (representing a credit memo statement)
        account.setCurrentPending(account.getCurrentPending().subtract(request.getAmountPaid()));

        // 4. Automated Status Revocation Guard
        // If a player was previously restricted/flagged due to debt, dropping below zero/pending reactive balances restores standing
        if (account.getCurrentPending().compareTo(account.getOutstandings().negate()) <= 0) {
            account.setStatus(true);
        }

        accountRepository.save(account);

        // 5. Construct and return clean transaction data payload
        return PaymentResponse.builder()
                .id(savedPayment.getId())
                .amountPaid(savedPayment.getAmountPaid())
                .paymentMethod(savedPayment.getPaymentMethod())
                .referenceNumber(savedPayment.getReferenceNumber())
                .paymentDate(savedPayment.getPaymentDate())
                .build();
    }
}