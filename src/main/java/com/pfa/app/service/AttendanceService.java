package com.pfa.app.service;


import com.pfa.app.dto.request.BatchAttendanceRequest;

public interface AttendanceService {
    /**
     * Processes a full training or match attendance sheet, instantiating
     * the event master row and automatically cascading the individual status lines.
     *
     * @param request The batch request containing event details and player status records.
     */
    void submitBatchAttendance(BatchAttendanceRequest request);
}