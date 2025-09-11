package com.ijaa.user.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String userId;
    private String refreshToken;
    
    // Constructor for backward compatibility
    public AuthResponse(String accessToken, String userId) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.tokenType = "Bearer";
    }
    
    // Constructor with refresh token
    public AuthResponse(String accessToken, String userId, String refreshToken) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.tokenType = "Bearer";
        this.refreshToken = refreshToken;
    }
}
