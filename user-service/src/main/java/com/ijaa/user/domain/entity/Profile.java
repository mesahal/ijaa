package com.ijaa.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

    private Boolean showPhone = true;

    private Boolean showLinkedIn = true;

    private Boolean showWebsite = true;

    private Boolean showEmail = true;

    private Boolean showFacebook = true;

    private Integer connections = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
