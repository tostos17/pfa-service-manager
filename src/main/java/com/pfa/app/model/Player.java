package com.pfa.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

    @Id
    private Long id;

    @Column(name = "player_id", nullable = false)
    private String playerId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "middle_name", nullable = false, length = 50)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(unique = true, length = 100)
    private String email;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "state_of_origin")
    private String stateOfOrigin;

    @Column(name = "country")
    private String country;

    @Column(name = "healthy")
    private boolean healthy;

    @Column(name = "health_concerns")
    private String healthConcernDescription;

    // Added field to capture the S3 bucket photo reference location
    @Column(name = "passport_url", length = 500)
    private String passportUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PlayerProfile playerProfile;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Account account;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;
}