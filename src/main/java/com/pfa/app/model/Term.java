package com.pfa.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "terms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(nullable = false, length = 30)
    private String name; // E.g., "FIRST", "SECOND", "THIRD"

    @Column(name = "syllabus_url", length = 500)
    private String syllabusUrl;

    @Column(name = "matches_description", columnDefinition = "TEXT")
    private String matchesDescription;

    @Column(name = "assessment_description", columnDefinition = "TEXT")
    private String assessmentDescription;

    @Column(name = "results_summary", columnDefinition = "TEXT")
    private String resultsSummary;

    @Column(name = "opening_account_balance", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal openingAccountBalance = BigDecimal.ZERO;

    @Column(name = "closing_account_balance", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal closingAccountBalance = BigDecimal.ZERO;
}