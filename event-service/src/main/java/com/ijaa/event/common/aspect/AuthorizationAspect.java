package com.ijaa.event.common.aspect;

import com.ijaa.event.common.annotation.RequiresRole;
import com.ijaa.event.common.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationAspect {

    private final AuthorizationService authorizationService;

    @Around("@annotation(requiresRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequiresRole requiresRole) throws Throwable {
        // Check if user is authenticated
        if (!authorizationService.isAuthenticated()) {
            log.warn("Unauthorized access attempt to method: {}", joinPoint.getSignature().getName());
            return ResponseEntity.status(401).body(
                new com.ijaa.event.domain.common.ApiResponse<>("Authentication required", "401", null)
            );
        }

        // Check if user has required role
        if (!authorizationService.hasRole(requiresRole.value())) {
            log.warn("Access denied for role {} to method: {}", requiresRole.value(), joinPoint.getSignature().getName());
            return ResponseEntity.status(403).body(
                new com.ijaa.event.domain.common.ApiResponse<>("Access denied. " + requiresRole.value() + " role required", "403", null)
            );
        }

        // User is authenticated and has required role, proceed with method execution
        return joinPoint.proceed();
    }
}
