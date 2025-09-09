package com.ijaa.event.domain.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ProfileDto {
    @NotBlank(message = "UserId must not be blank")
    private String userId;
    @NotBlank(message = "Name must not be blank")
    private String name;
    @NotBlank(message = "Profession must not be blank")
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
