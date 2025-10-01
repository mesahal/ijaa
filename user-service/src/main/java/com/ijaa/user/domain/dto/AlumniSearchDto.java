package com.ijaa.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "Alumni search result data transfer object")
public class AlumniSearchDto {
    
    @Schema(description = "Unique user identifier", example = "user123")
    private String userId;
    
    @Schema(description = "Full name of the alumni", example = "John Doe")
    private String name;
    
    @Schema(description = "Graduation batch year", example = "2020")
    private String batch;
    
    @Schema(description = "Current profession or job title", example = "Software Engineer")
    private String profession;
    
    @Schema(description = "City ID", example = "163")
    private Long cityId;
    
    @Schema(description = "Country ID", example = "101")
    private Long countryId;
    
    @Schema(description = "City name", example = "Bangalore")
    private String cityName;
    
    @Schema(description = "Country name", example = "India")
    private String countryName;
    
    @Schema(description = "Profile avatar URL", example = "https://example.com/avatar.jpg")
    private String avatar;
    
    @Schema(description = "Short bio or description", example = "Passionate software engineer with 3+ years of experience")
    private String bio;
    
    @Schema(description = "Number of connections", example = "45")
    private Integer connections;
    
    @Schema(description = "Whether the current user is connected to this alumni", example = "false")
    private Boolean isConnected;
    
    @Schema(description = "List of interests and skills", example = "[\"Java\", \"Spring Boot\", \"Microservices\"]")
    private List<String> interests;
}
