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
    public List<CityDto> getCitiesByCountryId(Long countryId) {
        return cityRepository.findByCountryIdOrderByName(countryId)
                .stream()
                .map(this::toCityDto)
                .collect(Collectors.toList());
    }


    private CountryDto toCountryDto(Country country) {
        CountryDto dto = new CountryDto();
        dto.setId(country.getId());
        dto.setName(country.getName());
        return dto;
    }

    private CityDto toCityDto(City city) {
        CityDto dto = new CityDto();
        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setCountryId(city.getCountryId());
        return dto;
    }
}
