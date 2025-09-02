package com.ijaa.user.service.impl;

import com.ijaa.user.domain.dto.CityDto;
import com.ijaa.user.domain.dto.CountryDto;
import com.ijaa.user.domain.entity.City;
import com.ijaa.user.domain.entity.Country;
import com.ijaa.user.repository.CityRepository;
import com.ijaa.user.repository.CountryRepository;
import com.ijaa.user.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CountryDto> getAllCountries() {
        return countryRepository.findAllActiveOrderByName()
                .stream()
                .map(this::toCountryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CountryDto getCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found"));
        return toCountryDto(country);
    }

    @Override
    @Transactional(readOnly = true)
    public CountryDto getCountryByIso2(String iso2) {
        Country country = countryRepository.findByIso2(iso2)
                .orElseThrow(() -> new RuntimeException("Country not found"));
        return toCountryDto(country);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryDto> searchCountries(String searchTerm) {
        return countryRepository.findByNameContainingIgnoreCase(searchTerm)
                .stream()
                .map(this::toCountryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityDto> getCitiesByCountryId(Long countryId) {
        return cityRepository.findByCountryIdOrderByName(countryId)
                .stream()
                .map(this::toCityDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityDto> searchCitiesByCountry(Long countryId, String searchTerm) {
        return cityRepository.findByCountryIdAndNameContainingIgnoreCase(countryId, searchTerm)
                .stream()
                .map(this::toCityDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityDto> searchCities(String searchTerm) {
        return cityRepository.findByNameContainingIgnoreCase(searchTerm)
                .stream()
                .map(this::toCityDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CityDto getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found"));
        return toCityDto(city);
    }

    private CountryDto toCountryDto(Country country) {
        CountryDto dto = new CountryDto();
        dto.setId(country.getId());
        dto.setName(country.getName());
        dto.setIso2(country.getIso2());
        dto.setIso3(country.getIso3());
        dto.setEmoji(country.getEmoji());
        dto.setFlag(country.getFlag() != null ? country.getFlag().toString() : "1");
        return dto;
    }

    private CityDto toCityDto(City city) {
        CityDto dto = new CityDto();
        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setCountryId(city.getCountryId());
        dto.setCountryCode(city.getCountryCode());
        dto.setStateId(city.getStateId());
        dto.setStateCode(city.getStateCode());
        return dto;
    }
}
