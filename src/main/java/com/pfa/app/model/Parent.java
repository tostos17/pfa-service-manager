package com.pfa.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parent {

    @Id
    private Long id; // Will match the user_id exactly

    @Column(length = 20)
    private String title; // Mr, Mrs, Dr, etc.

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Specifies that the parent's ID is derived from the user association
    @JoinColumn(name = "user_id")
    private User user;
}
