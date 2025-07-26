package com.ijaa.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String name;
    private String profession;
    private String location;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String phone;
    private String linkedIn;
    private String website;
    private String batch;
    private String department;
    private String email;

    @Builder.Default
    private Boolean showPhone = true;

    @Builder.Default
    private Boolean showLinkedIn = true;

    @Builder.Default
    private Boolean showWebsite = true;

    @Builder.Default
    private Boolean showEmail = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
