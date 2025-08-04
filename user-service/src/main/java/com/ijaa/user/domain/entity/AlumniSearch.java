package com.ijaa.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alumni_profiles")
public class AlumniSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String name;
    private String batch;
    private String department;
    private String profession;
    private String company;
    private String location;
    private String avatar;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private Integer connections = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "alumni_skills", joinColumns = @JoinColumn(name = "alumni_id"))
    @Column(name = "skill")
    private List<String> skills;

    private Boolean isVisible = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
