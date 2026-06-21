package com.pfa.app.service.impl;

import com.pfa.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class AdminIdentifierService {

    private final UserRepository userRepository;
    private static final String ADMIN_PREFIX = "ADM";

    public AdminIdentifierService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Atomically generates a unique Admin Employee ID.
     * Output format example: ADM-2026-0001
     */
    @Transactional
    public String generateAdminEmployeeId() {
        // 1. Fetch the thread-safe atomic counter from PostgreSQL
        Long sequenceNum = userRepository.getNextAdminSequenceValue();

        // 2. Extract the current calendar year
        int currentYear = LocalDate.now().getYear();

        // 3. Format into a structured string padding the sequence to 4 digits
        return String.format("%s-%d-%04d", ADMIN_PREFIX, currentYear, sequenceNum);
    }
}