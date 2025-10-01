package com.ijaa.user.presenter.rest.api;

import com.ijaa.user.common.annotation.RequiresFeature;
import com.ijaa.user.domain.dto.CityDto;
import com.ijaa.user.domain.dto.CountryDto;
import com.ijaa.user.service.LocationService;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.common.utils.AppUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppUtils.LOCATIONS_BASE_URL)
@RequiredArgsConstructor
@Tag(name = "Location Management")
public class LocationResource {

    private final LocationService locationService;

    @GetMapping("/countries")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("user.location")
    @Operation(
        summary = "Get All Countries", 
        description = "Get list of all active countries sorted alphabetically"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Countries retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        summary = "List of countries",
                        value = """
                            {
                                "message": "Countries fetched successfully",
                                "status": "200",
                                "data": [
                                    {
                                        "id": 1,
                                        "name": "Afghanistan"
                                    },
                                    {
                                        "id": 2,
                                        "name": "Albania"
                                    }
                                ]
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<List<CountryDto>>> getAllCountries() {
        List<CountryDto> countries = locationService.getAllCountries();
        return ResponseEntity.ok(
                new ApiResponse<>("Countries fetched successfully", "200", countries)
        );
    }


    @GetMapping("/countries/{countryId}/cities")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("user.location")
    @Operation(
        summary = "Get Cities by Country", 
        description = "Get list of cities for a specific country, sorted alphabetically"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Cities retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Cities List",
                        summary = "Cities in the specified country",
                        value = """
                            {
                                "message": "Cities fetched successfully",
                                "status": "200",
                                "data": [
                                    {
                                        "id": 1,
                                        "name": "New York",
                                        "countryId": 1
                                    },
                                    {
                                        "id": 2,
                                        "name": "Los Angeles",
                                        "countryId": 1
                                    }
                                ]
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<List<CityDto>>> getCitiesByCountry(
            @Parameter(description = "Country ID (e.g., 1 for United States)") 
            @PathVariable Long countryId) {
        List<CityDto> cities = locationService.getCitiesByCountryId(countryId);
        return ResponseEntity.ok(
                new ApiResponse<>("Cities fetched successfully", "200", cities)
        );
    }

}
