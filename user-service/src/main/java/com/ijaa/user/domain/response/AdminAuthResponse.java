package com.ijaa.user.domain.response;

import com.ijaa.user.domain.enums.AdminRole;
import lombok.Data;

@Data
public class AdminAuthResponse {
    private String accessToken;
    private Long adminId;
    private String name;
    private String email;
    private AdminRole role;
    private Boolean active;
} 