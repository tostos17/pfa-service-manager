package com.pfa.app.service.impl;

import com.pfa.app.dto.request.CloseTermAuditRequest;
import com.pfa.app.model.Account;
import com.pfa.app.model.Term;
import com.pfa.app.repository.AccountRepository;
import com.pfa.app.repository.TermRepository;
import com.pfa.app.service.FinancialAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialAuditServiceImpl implements FinancialAuditService {

    private final TermRepository termRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void executeTermRolloverAudit(CloseTermAuditRequest request) {
        // 1. Locate the closing Term entity
        Term closingTerm = termRepository.findById(request.getTermId())
                .orElseThrow(() -> new IllegalArgumentException("Audit failed: Target Term not found with ID: " + request.getTermId()));

        // 2. Pull all active player account ledgers across the entire academy
        List<Account> allAccounts = accountRepository.findAll();

        BigDecimal totalAcademyUnpaidClosingBalance = BigDecimal.ZERO;

        // 3. Process each account ledger sequentially
        for (Account account : allAccounts) {
            BigDecimal remainingTermDebt = account.getCurrentPending();

            if (remainingTermDebt.compareTo(BigDecimal.ZERO) > 0) {
                // Accumulate total academy debt for this term's audit metrics
                totalAcademyUnpaidClosingBalance = totalAcademyUnpaidClosingBalance.add(remainingTermDebt);

                // Shift current unpaid debt into outstandings (previous terms debt tracker)
                account.setOutstandings(account.getOutstandings().add(remainingTermDebt));
            }

            // Reset current pending balance back to zero, ready for the next term's invoices
            account.setCurrentPending(BigDecimal.ZERO);

            // Save the updated player account state
            accountRepository.save(account);
        }

        // 4. Stamp and finalize the Term's absolute ending financial footprint
        closingTerm.setClosingAccountBalance(totalAcademyUnpaidClosingBalance);
        termRepository.save(closingTerm);
    }
}