package com.ijaa.user.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ijaa.user.common.exceptions.GoogleOAuthException;
import com.ijaa.user.domain.dto.GoogleUserInfo;
import com.ijaa.user.domain.request.GoogleSignInRequest;
import com.ijaa.user.service.GoogleOAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthServiceImpl implements GoogleOAuthService {

    @Value("${google.oauth2.client-id}")
    private String googleClientId;

    @Value("${google.oauth2.client-secret}")
    private String googleClientSecret;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public GoogleUserInfo verifyGoogleToken(GoogleSignInRequest request) {
        try {
            // Verify the ID token first
            GoogleUserInfo userInfo = verifyIdToken(request.getIdToken());
            
            // Additional verification with access token if needed
            if (request.getGoogleToken() != null && !request.getGoogleToken().isEmpty()) {
                verifyAccessToken(request.getGoogleToken());
            }
            
            return userInfo;
        } catch (Exception e) {
            log.error("Error verifying Google token: {}", e.getMessage(), e);
            throw new GoogleOAuthException("Failed to verify Google OAuth token: " + e.getMessage());
        }
    }

    @Override
    public GoogleUserInfo verifyIdToken(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), 
                    new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            
            if (googleIdToken == null) {
                throw new GoogleOAuthException("Invalid Google ID token");
            }

            Payload payload = googleIdToken.getPayload();
            
            GoogleUserInfo userInfo = new GoogleUserInfo();
            userInfo.setSub(payload.getSubject());
            userInfo.setEmail(payload.getEmail());
            userInfo.setName((String) payload.get("name"));
            userInfo.setGivenName((String) payload.get("given_name"));
            userInfo.setFamilyName((String) payload.get("family_name"));
            userInfo.setPicture((String) payload.get("picture"));
            userInfo.setLocale((String) payload.get("locale"));
            userInfo.setEmailVerified(payload.getEmailVerified());
            userInfo.setHd((String) payload.get("hd"));
            
            log.info("Successfully verified Google ID token for user: {}", userInfo.getEmail());
            return userInfo;
            
        } catch (Exception e) {
            log.error("Error verifying Google ID token: {}", e.getMessage(), e);
            throw new GoogleOAuthException("Failed to verify Google ID token: " + e.getMessage());
        }
    }

    @Override
    public String getGoogleOAuthConfig() {
        return String.format("{\"clientId\":\"%s\"}", googleClientId);
    }

    private void verifyAccessToken(String accessToken) {
        try {
            String url = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=" + accessToken;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new GoogleOAuthException("Invalid access token");
            }
            
            JsonNode responseBody = objectMapper.readTree(response.getBody());
            String aud = responseBody.get("aud").asText();
            
            if (!googleClientId.equals(aud)) {
                throw new GoogleOAuthException("Access token audience mismatch");
            }
            
            log.debug("Successfully verified Google access token");
            
        } catch (Exception e) {
            log.error("Error verifying Google access token: {}", e.getMessage(), e);
            throw new GoogleOAuthException("Failed to verify Google access token: " + e.getMessage());
        }
    }
}
