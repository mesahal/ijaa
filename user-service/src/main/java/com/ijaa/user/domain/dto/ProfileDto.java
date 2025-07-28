package com.ijaa.user.domain.dto;

import lombok.Data;

@Data
public class ProfileDto {
    private String userId;
    private String name;
    private String profession;
    private String location;
    private String bio;
    private String phone;
    private String linkedIn;
    private String website;
    private String batch;
    private String facebook;
    private String email;
    private Boolean showPhone;
    private Boolean showLinkedIn;
    private Boolean showWebsite;
    private Boolean showEmail;
    private Boolean showFacebook;
    private Integer connections;
}
