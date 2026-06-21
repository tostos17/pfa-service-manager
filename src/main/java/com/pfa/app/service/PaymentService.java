package com.pfa.app.service;

import com.pfa.app.dto.request.ProcessPaymentRequest;
import com.pfa.app.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPlayerPayment(String playerId, ProcessPaymentRequest request);
}