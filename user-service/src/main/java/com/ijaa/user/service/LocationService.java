package com.ijaa.user.service;

import com.ijaa.user.domain.dto.CityDto;
import com.ijaa.user.domain.dto.CountryDto;

import java.util.List;

public interface LocationService {
    
    // Country methods
    List<CountryDto> getAllCountries();
    CountryDto getCountryById(Long id);
    CountryDto getCountryByIso2(String iso2);
    List<CountryDto> searchCountries(String searchTerm);
    
    // City methods
    List<CityDto> getCitiesByCountryId(Long countryId);
    List<CityDto> searchCitiesByCountry(Long countryId, String searchTerm);
    List<CityDto> searchCities(String searchTerm);
    CityDto getCityById(Long id);
}
