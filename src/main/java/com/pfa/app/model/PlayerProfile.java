package com.pfa.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "player_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerProfile {

    @Id
    private Long id; // Maps exactly to the PlayerData database primary key id

    @Column(name = "height_cm", nullable = false)
    private Double heightCm;

    @Column(name = "weight_kg", nullable = false)
    private Double weightKg;

    @Column(name = "dominant_foot", nullable = false, length = 10)
    private String dominantFoot;

    @Column(nullable = false, length = 30)
    private String position;

    @Column(name = "preferred_jersey_number")
    private Integer preferredJerseyNumber;

    @Column(columnDefinition = "TEXT")
    private String biography;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;
}