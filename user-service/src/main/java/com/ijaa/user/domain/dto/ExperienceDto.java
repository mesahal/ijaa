package com.ijaa.user.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class ExperienceDto {
    private Long id;
    private String userId;
    @NotBlank(message = "Title must not be blank")
    private String title;
    @NotBlank(message = "Company must not be blank")
    private String company;
    private String period;
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    private LocalDateTime createdAt;
}
