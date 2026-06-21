package com.pfa.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(name = "match_time", nullable = false)
    private String matchTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scorer_id", nullable = false)
    private Player scorer;

    @Column(name = "goal_count")
    private int count; // Overall match running total marker or player seasonal tally index

    @Column(name = "is_freekick", nullable = false)
    private boolean isFreekick;

    @Column(name = "is_penalty", nullable = false)
    private boolean isPenalty;

    @Column(name = "is_own_goal", nullable = false)
    private boolean isOwnGoal;
}
