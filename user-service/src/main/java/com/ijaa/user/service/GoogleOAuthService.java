package com.ijaa.user.service;

import com.ijaa.user.domain.dto.GoogleUserInfo;
import com.ijaa.user.domain.request.GoogleSignInRequest;

public interface GoogleOAuthService {
    
    /**
     * Verify Google OAuth token and extract user information
     * @param request Google sign-in request containing tokens
     * @return Google user information
     */
    GoogleUserInfo verifyGoogleToken(GoogleSignInRequest request);
    
    /**
     * Verify Google ID token
     * @param idToken Google ID token
     * @return Google user information
     */
    GoogleUserInfo verifyIdToken(String idToken);
    
    /**
     * Get Google OAuth configuration
     * @return Google OAuth client configuration
     */
    String getGoogleOAuthConfig();
}
