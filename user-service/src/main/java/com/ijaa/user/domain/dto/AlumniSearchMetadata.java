package com.ijaa.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Metadata for alumni search including available filters and pagination limits")
public class AlumniSearchMetadata {
    
    @Schema(description = "Total number of alumni available for search", example = "1250")
    private long totalAlumni;
    
    @Schema(description = "Available graduation batch years", example = "[\"2018\", \"2019\", \"2020\", \"2021\", \"2022\", \"2023\"]")
    private List<String> availableBatches;
    
    @Schema(description = "Available professions/industries", example = "[\"Technology\", \"Finance\", \"Healthcare\", \"Education\"]")
    private List<String> availableProfessions;
    
    @Schema(description = "Available cities", example = "[\"Bangalore\", \"Mumbai\", \"Delhi\", \"Chennai\", \"Hyderabad\"]")
    private List<String> availableCities;
    
    @Schema(description = "Available countries", example = "[\"India\", \"United States\", \"United Kingdom\", \"Canada\"]")
    private List<String> availableCountries;
    
    @Schema(description = "Default page size for search results", example = "12")
    private int defaultPageSize;
    
    @Schema(description = "Maximum allowed page size", example = "100")
    private int maxPageSize;
    
    @Schema(description = "Maximum allowed page number", example = "1000")
    private int maxPageNumber;
    
    @Schema(description = "Available sort options", example = "[\"relevance\", \"name\", \"batch\", \"connections\"]")
    private List<String> availableSortOptions;
}
