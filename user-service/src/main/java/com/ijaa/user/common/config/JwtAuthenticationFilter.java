package com.ijaa.user.common.config;

import com.ijaa.user.service.AdminUserDetailsService;
import com.ijaa.user.service.JWTService;
import com.ijaa.user.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final AdminUserDetailsService adminUserDetailsService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userType;
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        jwt = authHeader.substring(7);
        
        try {
            // Extract user type from JWT
            userType = jwtService.extractUserType(jwt);
            
            if (userType != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = null;
                
                if ("ADMIN".equals(userType)) {
                    // Handle admin authentication
                    String email = jwtService.extractEmail(jwt);
                    if (email != null) {
                        userDetails = adminUserDetailsService.loadUserByUsername(email);
                    }
                } else {
                    // Handle user authentication
                    String username = jwtService.extractUsername(jwt);
                    if (username != null) {
                        userDetails = userDetailsService.loadUserByUsername(username);
                    }
                }
                
                if (userDetails != null && jwtService.validateToken(jwt, jwtSecret)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            log.error("JWT authentication error: {}", e.getMessage());
            // Don't throw exception, just continue without authentication
        }
        
        filterChain.doFilter(request, response);
    }
} 