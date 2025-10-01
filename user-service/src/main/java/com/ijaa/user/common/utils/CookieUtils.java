package com.ijaa.user.common.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling HTTP cookies (Refresh Token).
 *
 * NOTE:
 * - For localhost/dev (no HTTPS): Secure=false, SameSite=Lax
 * - For production (with HTTPS): Secure=true, SameSite=Strict (or Lax if needed)
 */
@Component
@Slf4j
public class CookieUtils {

    @Value("${jwt.refresh-token-cookie-name:refreshToken}")
    private String refreshTokenCookieName;
    
    @Value("${jwt.refresh-token-cookie-path:/}")
    private String cookiePath;
    
    @Value("${jwt.refresh-token-expiration}")
    private Long cookieMaxAgeSeconds;

    /**
     * Extract refresh token from cookie
     */
    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (refreshTokenCookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Set refresh token cookie
     */
    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken, boolean isProd) {
        String sameSite = isProd ? "Strict" : "Lax"; // 🔹 Strict for prod, Lax for dev
        boolean secure = isProd;                     // 🔹 Secure=true only in prod (HTTPS)

        String cookieValue = String.format(
                "%s=%s; Path=%s; HttpOnly; Max-Age=%d; SameSite=%s%s",
                refreshTokenCookieName,
                refreshToken,
                cookiePath,
                cookieMaxAgeSeconds,
                sameSite,
                secure ? "; Secure" : "" // add Secure only if prod
        );

        response.addHeader("Set-Cookie", cookieValue);

        log.info("Set refresh token cookie: name={}, path={}, maxAge={}, httpOnly=true, secure={}, sameSite={}",
                refreshTokenCookieName, cookiePath, cookieMaxAgeSeconds, secure, sameSite);
    }

    /**
     * Clear refresh token cookie
     */
    public void clearRefreshTokenCookie(HttpServletResponse response, boolean isProd) {
        String sameSite = isProd ? "Strict" : "Lax";
        boolean secure = isProd;

        String cookieValue = String.format(
                "%s=; Path=%s; HttpOnly; Max-Age=0; SameSite=%s%s",
                refreshTokenCookieName,
                cookiePath,
                sameSite,
                secure ? "; Secure" : ""
        );

        response.addHeader("Set-Cookie", cookieValue);

        log.info("Cleared refresh token cookie: name={}, path={}, secure={}, sameSite={}",
                refreshTokenCookieName, cookiePath, secure, sameSite);
    }
}
