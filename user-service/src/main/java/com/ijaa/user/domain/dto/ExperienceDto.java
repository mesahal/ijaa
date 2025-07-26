package com.ijaa.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDto {
    private Long id;
    private String title;
    private String company;
    private String period;
    private String description;
}
