package com.pfa.app.repository;

import com.pfa.app.model.PlayerAttendanceLine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlayerAttendanceLineRepository extends JpaRepository<PlayerAttendanceLine, Long> {
    List<PlayerAttendanceLine> findByPlayerPlayerId(String playerId);
}