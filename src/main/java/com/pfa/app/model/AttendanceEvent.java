package com.pfa.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attendance_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "event_type", nullable = false, length = 30)
    private String eventType;

    private String description;

    @OneToMany(mappedBy = "attendanceEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PlayerAttendanceLine> attendanceLines = new ArrayList<>();
}