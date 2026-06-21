package com.pfa.app.service.impl;

import com.pfa.app.dto.request.BatchAttendanceRequest;
import com.pfa.app.dto.request.PlayerAttendanceSubmitDTO;
import com.pfa.app.model.AttendanceEvent;
import com.pfa.app.model.Player;
import com.pfa.app.repository.AttendanceEventRepository;
import com.pfa.app.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceImplTest {

    @Mock private AttendanceEventRepository eventRepository;
    @Mock private PlayerRepository playerDataRepository;

    @InjectMocks private AttendanceServiceImpl attendanceService;

    @Test
    void submitBatchAttendance_Success() {
        // Arrange
        PlayerAttendanceSubmitDTO record = PlayerAttendanceSubmitDTO.builder()
                .playerId("player-uuid")
                .status("PRESENT")
                .remarks("On time")
                .build();

        BatchAttendanceRequest request = BatchAttendanceRequest.builder()
                .eventDate(LocalDate.now())
                .eventType("TRAINING")
                .description("Tactical work")
                .records(List.of(record))
                .build();

        Player player = new Player();

        when(playerDataRepository.findByPlayerId("player-uuid")).thenReturn(Optional.of(player));

        // Act
        assertDoesNotThrow(() -> attendanceService.submitBatchAttendance(request));

        // Assert
        verify(eventRepository, times(1)).save(any(AttendanceEvent.class));
    }

    @Test
    void submitBatchAttendance_PlayerNotFound_ThrowsException() {
        // Arrange
        PlayerAttendanceSubmitDTO record = PlayerAttendanceSubmitDTO.builder()
                .playerId("invalid-uuid")
                .status("PRESENT")
                .build();

        BatchAttendanceRequest request = BatchAttendanceRequest.builder()
                .eventDate(LocalDate.now())
                .eventType("TRAINING")
                .records(List.of(record))
                .build();

        when(playerDataRepository.findByPlayerId("invalid-uuid")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> attendanceService.submitBatchAttendance(request));
        verify(eventRepository, never()).save(any());
    }
}