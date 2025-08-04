package com.ijaa.user.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "jwt.secret=16f7e412ee66030c3bf769281a076955f595be7479189c4e5ab1f90d2ae3c82e0c5170afcceba1e0f638648c01a468ff82b0723a970011f7fc0dd1a4ba70b0e1"
})
public class JWTServiceTest {

    @Autowired
    private JWTService jwtService;

    @Test
    public void testGenerateUserToken() {
        String username = "testuser";
        String token = jwtService.generateUserToken(username);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verify claims
        String extractedUsername = jwtService.extractUsername(token);
        String userType = jwtService.extractUserType(token);
        String role = jwtService.extractRole(token);
        
        assertEquals(username, extractedUsername);
        assertEquals("ALUMNI", userType);
        assertEquals("USER", role);
    }

    @Test
    public void testGenerateAdminToken() {
        String email = "admin@test.com";
        String role = "ADMIN";
        String token = jwtService.generateAdminToken(email, role);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verify claims
        String extractedEmail = jwtService.extractUsername(token); // Subject is email for admin
        String userType = jwtService.extractUserType(token);
        String extractedRole = jwtService.extractRole(token);
        
        assertEquals(email, extractedEmail);
        assertEquals("ADMIN", userType);
        assertEquals(role, extractedRole);
    }

    @Test
    public void testTokenExpiration() {
        String token = jwtService.generateUserToken("testuser");
        
        // Token should be valid initially
        assertTrue(jwtService.isTokenValid(token, new org.springframework.security.core.userdetails.User("testuser", "", java.util.Collections.emptyList())));
    }
} 