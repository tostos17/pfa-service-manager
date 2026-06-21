package com.pfa.app.service;

import com.pfa.app.dto.request.CreateInvoiceRequest;
import com.pfa.app.dto.response.InvoiceResponse;

public interface InvoiceService {
    InvoiceResponse issueInvoiceToPlayer(String playerId, CreateInvoiceRequest request);
}