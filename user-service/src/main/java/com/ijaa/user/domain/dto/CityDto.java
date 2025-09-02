package com.ijaa.user.domain.dto;

import lombok.Data;

@Data
public class CityDto {
    private Long id;
    private String name;
    private Long countryId;
    private String countryCode;
    private Long stateId;
    private String stateCode;
}
