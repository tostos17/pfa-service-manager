package com.pfa.app.controller;

import com.pfa.app.model.Session;
import com.pfa.app.service.AcademyCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/admin/calendar")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AcademyCalendarController {

    private final AcademyCalendarService calendarService;

    @PostMapping("/sessions")
    public ResponseEntity<String> createSession(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String activities
    ) {
        Session savedSession = calendarService.initializeNewSession(name, startDate, endDate, activities);
        return ResponseEntity.ok("Session " + savedSession.getName() + " initialized cleanly with 3 terms.");
    }
}