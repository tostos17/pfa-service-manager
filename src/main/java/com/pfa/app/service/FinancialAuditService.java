package com.pfa.app.service;


import com.pfa.app.dto.request.CloseTermAuditRequest;

public interface FinancialAuditService {
    void executeTermRolloverAudit(CloseTermAuditRequest request);
}