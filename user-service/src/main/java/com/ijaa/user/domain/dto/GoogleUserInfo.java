package com.ijaa.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserInfo {
    private String sub; // Google user ID
    private String email;
    private String name;
    private String givenName; // First name
    private String familyName; // Last name
    private String picture; // Profile picture URL
    private String locale;
    private Boolean emailVerified;
    private String hd; // Hosted domain (for G Suite users)
}
