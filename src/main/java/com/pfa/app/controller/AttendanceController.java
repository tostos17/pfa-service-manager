package com.pfa.app.controller;

import com.pfa.app.dto.request.BatchAttendanceRequest;
import com.pfa.app.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'COACH')")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/batch")
    public ResponseEntity<String> submitRosterAttendance(@Valid @RequestBody BatchAttendanceRequest request) {
        attendanceService.submitBatchAttendance(request);
        return ResponseEntity.ok("Roster training attendance logged successfully.");
    }
}