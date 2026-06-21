package com.pfa.app.service;

import com.pfa.app.model.Session;
import java.time.LocalDate;

public interface AcademyCalendarService {
    Session initializeNewSession(String sessionName, LocalDate startDate, LocalDate endDate, String activities);
}