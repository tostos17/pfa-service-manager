package com.pfa.app.service.impl;

import com.pfa.app.model.Session;
import com.pfa.app.model.Term;
import com.pfa.app.repository.SessionRepository;
import com.pfa.app.service.AcademyCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademyCalendarServiceImpl implements AcademyCalendarService {

    private final SessionRepository sessionRepository;

    @Override
    @Transactional
    public Session initializeNewSession(String sessionName, LocalDate startDate, LocalDate endDate, String activities) {
        if (sessionRepository.findByName(sessionName).isPresent()) {
            throw new IllegalArgumentException("Session " + sessionName + " has already been created.");
        }

        // 1. Build the base container entity
        Session session = Session.builder()
                .name(sessionName)
                .startDate(startDate)
                .endDate(endDate)
                .activitiesDescription(activities)
                .build();

        // 2. Pre-populate exactly three foundational sub-terms structurally
        Term firstTerm = Term.builder().name("FIRST").session(session).build();
        Term secondTerm = Term.builder().name("SECOND").session(session).build();
        Term thirdTerm = Term.builder().name("THIRD").session(session).build();

        session.setTerms(List.of(firstTerm, secondTerm, thirdTerm));

        // 3. Save the session container cascaded to database tables
        return sessionRepository.save(session);
    }
}