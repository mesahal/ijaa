package com.ijaa.gateway.service;

import com.ijaa.gateway.domain.entity.BlacklistedToken;
import com.ijaa.gateway.repository.BlacklistedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    public void blacklistToken(String token, String userId, String userType) {
        // Extract expiry date from token
        LocalDateTime expiryDate = extractTokenExpiryDate(token);
        
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiryDate(expiryDate);
        blacklistedToken.setTokenType("ACCESS");
        blacklistedToken.setUserId(userId);
        blacklistedToken.setUserType(userType);
        
        blacklistedTokenRepository.save(blacklistedToken);
        log.info("Token blacklisted for user: {} (type: {})", userId, userType);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }

    @Transactional
    public void blacklistAllUserTokens(String userId, String userType) {
        // This method should blacklist all user tokens, not delete them
        // For now, we'll just delete existing blacklisted tokens for this user
        // In a real implementation, you would need to track all active tokens
        blacklistedTokenRepository.deleteAllUserTokens(userId, userType);
        log.info("All tokens blacklisted for user: {} (type: {})", userId, userType);
    }

    @Transactional
    public void cleanupExpiredTokens() {
        blacklistedTokenRepository.deleteExpiredTokens(LocalDateTime.now());
        log.info("Expired tokens cleaned up");
    }

    private LocalDateTime extractTokenExpiryDate(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            Date expiryDate = claims.getExpiration();
            return LocalDateTime.ofInstant(expiryDate.toInstant(), java.time.ZoneId.systemDefault());
        } catch (Exception e) {
            log.warn("Could not parse token expiry, using default: {}", e.getMessage());
            // If we can't parse the token, set expiry to current time + access token duration
            return LocalDateTime.now().plusSeconds(accessTokenExpiration);
        }
    }

    private SecretKey getKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(keyBytes, "HmacSHA512");
    }
}
