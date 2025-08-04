package com.ijaa.user.domain.response;

import com.ijaa.user.domain.enums.AdminRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminProfileResponse {
    private Long id;
    private String name;
    private String email;
    private AdminRole role;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 