package com.ijaa.user.domain.dto;

import lombok.Data;

@Data
public class CountryDto {
    private Long id;
    private String name;
    private String iso2;
    private String iso3;
    private String emoji;
    private String flag;
}
