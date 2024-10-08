package com.iitju.ijaa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    private String username;
    private String password;
    private String email;
    private String registrationNo;
    private String firstName;
    private String lastName;
    private String gender;
    private String phone;
    private String currentCity;
    private String currentCountry;
    private String facebookProfileLink;
    private String twitterProfileLink;
    private String instagramProfileLink;
    private String linkedinProfileLink;
    private String universityBatch;
    private String currentJob;
    private String currentJobDesignation;
    private String currentCompanyName;
    private String instituteBatch;
    private boolean isActive;
}
