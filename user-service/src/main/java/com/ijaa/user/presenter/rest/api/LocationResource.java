package com.ijaa.user.presenter.rest.api;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppUtils.BASE_URL + "/location")
@RequiredArgsConstructor
@Tag(name = "Location Management", description = "APIs for countries and cities management")
public class LocationResource {

    private final LocationService locationService;

    @GetMapping("/countries")
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
                                        "name": "Afghanistan",
                                        "iso2": "AF",
                                        "iso3": "AFG",
                                        "emoji": "🇦🇫",
                                        "flag": "1"
                                    },
                                    {
                                        "id": 2,
                                        "name": "Albania",
                                        "iso2": "AL",
                                        "iso3": "ALB",
                                        "emoji": "🇦🇱",
                                        "flag": "1"
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

    @GetMapping("/countries/search")
    @Operation(
        summary = "Search Countries", 
        description = "Search countries by name (case-insensitive partial match)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Countries search completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Search Results",
                        summary = "Countries matching search term",
                        value = """
                            {
                                "message": "Countries search completed",
                                "status": "200",
                                "data": [
                                    {
                                        "id": 1,
                                        "name": "United States",
                                        "iso2": "US",
                                        "iso3": "USA",
                                        "emoji": "🇺🇸",
                                        "flag": "1"
                                    },
                                    {
                                        "id": 2,
                                        "name": "United Kingdom",
                                        "iso2": "GB",
                                        "iso3": "GBR",
                                        "emoji": "🇬🇧",
                                        "flag": "1"
                                    }
                                ]
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<List<CountryDto>>> searchCountries(
            @Parameter(description = "Search term for country name (e.g., 'United', 'America')") 
            @RequestParam String searchTerm) {
        List<CountryDto> countries = locationService.searchCountries(searchTerm);
        return ResponseEntity.ok(
                new ApiResponse<>("Countries search completed", "200", countries)
        );
    }

    @GetMapping("/countries/{id}")
    @Operation(summary = "Get Country by ID", description = "Get country details by ID")
    public ResponseEntity<ApiResponse<CountryDto>> getCountryById(
            @Parameter(description = "Country ID") 
            @PathVariable Long id) {
        CountryDto country = locationService.getCountryById(id);
        return ResponseEntity.ok(
                new ApiResponse<>("Country fetched successfully", "200", country)
        );
    }

    @GetMapping("/countries/iso2/{iso2}")
    @Operation(summary = "Get Country by ISO2", description = "Get country details by ISO2 code")
    public ResponseEntity<ApiResponse<CountryDto>> getCountryByIso2(
            @Parameter(description = "Country ISO2 code") 
            @PathVariable String iso2) {
        CountryDto country = locationService.getCountryByIso2(iso2);
        return ResponseEntity.ok(
                new ApiResponse<>("Country fetched successfully", "200", country)
        );
    }

    @GetMapping("/countries/{countryId}/cities")
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
                                        "countryId": 1,
                                        "countryCode": "US",
                                        "stateId": 1,
                                        "stateCode": "NY"
                                    },
                                    {
                                        "id": 2,
                                        "name": "Los Angeles",
                                        "countryId": 1,
                                        "countryCode": "US",
                                        "stateId": 2,
                                        "stateCode": "CA"
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

    @GetMapping("/countries/{countryId}/cities/search")
    @Operation(summary = "Search Cities by Country", description = "Search cities within a specific country")
    public ResponseEntity<ApiResponse<List<CityDto>>> searchCitiesByCountry(
            @Parameter(description = "Country ID") 
            @PathVariable Long countryId,
            @Parameter(description = "Search term for city name") 
            @RequestParam String searchTerm) {
        List<CityDto> cities = locationService.searchCitiesByCountry(countryId, searchTerm);
        return ResponseEntity.ok(
                new ApiResponse<>("Cities search completed", "200", cities)
        );
    }

    @GetMapping("/cities/search")
    @Operation(summary = "Search Cities", description = "Search cities globally")
    public ResponseEntity<ApiResponse<List<CityDto>>> searchCities(
            @Parameter(description = "Search term for city name") 
            @RequestParam String searchTerm) {
        List<CityDto> cities = locationService.searchCities(searchTerm);
        return ResponseEntity.ok(
                new ApiResponse<>("Cities search completed", "200", cities)
        );
    }

    @GetMapping("/cities/{id}")
    @Operation(summary = "Get City by ID", description = "Get city details by ID")
    public ResponseEntity<ApiResponse<CityDto>> getCityById(
            @Parameter(description = "City ID") 
            @PathVariable Long id) {
        CityDto city = locationService.getCityById(id);
        return ResponseEntity.ok(
                new ApiResponse<>("City fetched successfully", "200", city)
        );
    }
}
