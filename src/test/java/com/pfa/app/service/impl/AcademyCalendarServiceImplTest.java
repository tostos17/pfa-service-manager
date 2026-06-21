package com.pfa.app.service.impl;

import com.pfa.app.model.Session;
import com.pfa.app.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcademyCalendarServiceImplTest {

    @Mock private SessionRepository sessionRepository;
    @InjectMocks private AcademyCalendarServiceImpl calendarService;

    @Test
    void initializeNewSession_Success() {
        // Arrange
        String sessionName = "26/27";
        LocalDate start = LocalDate.of(2026, 9, 1);
        LocalDate end = LocalDate.of(2027, 6, 30);

        when(sessionRepository.findByName(sessionName)).thenReturn(Optional.empty());
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Session result = calendarService.initializeNewSession(sessionName, start, end, "Activities");

        // Assert
        assertNotNull(result);
        assertEquals(sessionName, result.getName());
        assertEquals(3, result.getTerms().size()); // Verifies the 3 sub-terms are created
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    void initializeNewSession_AlreadyExists_ThrowsException() {
        // Arrange
        String sessionName = "26/27";
        when(sessionRepository.findByName(sessionName)).thenReturn(Optional.of(new Session()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                calendarService.initializeNewSession(sessionName, LocalDate.now(), LocalDate.now(), "Activities")
        );
        verify(sessionRepository, never()).save(any());
    }
}