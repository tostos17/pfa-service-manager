package com.pfa.app.service.impl;

import com.pfa.app.dto.request.BatchAttendanceRequest;
import com.pfa.app.dto.request.PlayerAttendanceSubmitDTO;
import com.pfa.app.model.AttendanceEvent;
import com.pfa.app.model.Player;
import com.pfa.app.model.PlayerAttendanceLine;
import com.pfa.app.repository.AttendanceEventRepository;
import com.pfa.app.repository.PlayerRepository;
import com.pfa.app.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceEventRepository eventRepository;
    private final PlayerRepository playerDataRepository;

    @Override
    @Transactional
    public void submitBatchAttendance(BatchAttendanceRequest request) {
        // 1. Create the top-level Event master row
        AttendanceEvent event = AttendanceEvent.builder()
                .eventDate(request.getEventDate())
                .eventType(request.getEventType().toUpperCase())
                .description(request.getDescription())
                .build();

        // 2. Loop through and map each sheet record back to real player database rows
        for (PlayerAttendanceSubmitDTO record : request.getRecords()) {
            Player player = playerDataRepository.findByPlayerId(record.getPlayerId())
                    .orElseThrow(() -> new IllegalArgumentException("Attendance error: Player record not found for public ID: " + record.getPlayerId()));

            PlayerAttendanceLine line = PlayerAttendanceLine.builder()
                    .attendanceEvent(event)
                    .player(player)
                    .status(record.getStatus().toUpperCase())
                    .remarks(record.getRemarks())
                    .build();

            event.getAttendanceLines().add(line);
        }

        // 3. Persist the entire sheet downstream automatically via CascadeType.ALL
        eventRepository.save(event);
    }
}