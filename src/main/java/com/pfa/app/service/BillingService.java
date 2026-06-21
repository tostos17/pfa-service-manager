package com.pfa.app.service;


import com.pfa.app.dto.response.AccountStatementResponse;

public interface BillingService {
    AccountStatementResponse getPlayerStatement(String playerId);
}