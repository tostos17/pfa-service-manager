package com.pfa.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "match_events")
@Inheritance(strategy = InheritanceType.JOINED) // Keeps sub-class specific columns in separate tables
@DiscriminatorColumn(name = "event_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class MatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(name = "match_time", nullable = false)
    private String matchTime; // e.g., "45+2" or "12" to handle stoppage timeline metrics smoothly
}