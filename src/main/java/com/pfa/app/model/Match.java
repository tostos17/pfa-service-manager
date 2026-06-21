package com.pfa.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Category association: Null represents "GENERAL" category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Column(name = "opponent_name", nullable = false)
    private String opponentName;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String venue;

    @Builder.Default
    @Column(nullable = false)
    private boolean started = false;

    @Builder.Default
    @Column(name = "is_live", nullable = false)
    private boolean isLive = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean ended = false;

    @Column(name = "is_home_match", nullable = false)
    private boolean isHomeMatch;

    @Column(name = "is_away_match", nullable = false)
    private boolean isAwayMatch;

    @Builder.Default
    @Column(name = "home_team_score", nullable = false)
    private int homeTeamScore = 0;

    @Builder.Default
    @Column(name = "away_team_score", nullable = false)
    private int awayTeamScore = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_type", nullable = false)
    private MatchType type;

    @Column(name = "half_duration", nullable = false)
    @Builder.Default
    private int halfDuration = 45;

    // --- New Knockout / Extra Time State Fields ---

    @Builder.Default
    @Column(name = "is_extra_time", nullable = false)
    private boolean isExtraTime = false;

    @Builder.Default
    @Column(name = "is_penalty_shootout", nullable = false)
    private boolean isPenaltyShootout = false;

    @Builder.Default
    @Column(name = "home_team_penalty_score")
    private Integer homeTeamPenaltyScore = 0;

    @Builder.Default
    @Column(name = "away_team_penalty_score")
    private Integer awayTeamPenaltyScore = 0;

    // --- Media & Collections ---

    @ElementCollection
    @CollectionTable(name = "match_photos", joinColumns = @JoinColumn(name = "match_id"))
    @Column(name = "photo_url")
    @Builder.Default
    private List<String> photoUrls = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "match_videos", joinColumns = @JoinColumn(name = "match_id"))
    @Column(name = "video_url")
    @Builder.Default
    private List<String> videoUrls = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "match_starting_lineup",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @Builder.Default
    private List<Player> startingLineup = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("matchTime ASC")
    @Builder.Default
    private List<MatchEvent> events = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Goal> goalsScored = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Substitution> substitutions = new ArrayList<>();
}