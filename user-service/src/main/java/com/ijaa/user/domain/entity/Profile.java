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

    @Column(unique = true, nullable = false, length = 100)
    private String username;

    @Column(unique = true, nullable = false, length = 50)
    private String userId;

    @Column(length = 255)
    private String name;

    @Column(length = 255)
    private String profession;

    @Column(length = 255)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String linkedIn;

    @Column(length = 255)
    private String website;

    @Column(length = 10)
    private String batch;

    @Column(length = 255)
    private String email;

    @Column(length = 255)
    private String facebook;

    @Builder.Default
    private Boolean showPhone = true;

    @Builder.Default
    private Boolean showLinkedIn = true;

    @Builder.Default
    private Boolean showWebsite = true;

    @Builder.Default
    private Boolean showEmail = true;

    @Builder.Default
    private Boolean showFacebook = true;

    @Builder.Default
    private Integer connections = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
