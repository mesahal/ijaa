package com.ijaa.event_service.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String userId;
    private String username;
    private String name;
    private String email;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 