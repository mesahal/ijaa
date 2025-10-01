package com.ijaa.user.domain.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String userId;

    // Constructor for correct field assignment
    public AuthResponse(String accessToken, String userId) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.tokenType = "Bearer";
    }
}
