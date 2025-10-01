package com.ijaa.user.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String userId;
    private String username;
    private String name;
    private String email;
    private String profession;
    private Long cityId;
    private Long countryId;
    private String cityName;
    private String countryName;
    private String batch;
    private String phone;
    private String linkedIn;
    private String website;
    private String facebook;
    private String bio;
    private Integer connections;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 