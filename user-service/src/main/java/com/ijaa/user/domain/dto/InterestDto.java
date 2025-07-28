package com.ijaa.user.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterestDto {
    private Long id;
    private String userId;
    private String interest;
    private LocalDateTime createdAt;
}
