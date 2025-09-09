package com.ijaa.user.service;

import com.ijaa.user.domain.dto.CityDto;
import com.ijaa.user.domain.dto.CountryDto;

import java.util.List;

public interface LocationService {
    
    // Country methods
    List<CountryDto> getAllCountries();
    
    // City methods
    List<CityDto> getCitiesByCountryId(Long countryId);
}
