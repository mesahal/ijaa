package com.ijaa.user.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleSignInRequest {

    @NotBlank(message = "Google OAuth token is required")
    private String googleToken;

    @NotBlank(message = "Google ID token is required")
    private String idToken;
}
