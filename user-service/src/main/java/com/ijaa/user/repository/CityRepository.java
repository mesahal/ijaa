package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    
    @Query("SELECT c FROM City c WHERE c.countryId = :countryId ORDER BY c.name ASC")
    List<City> findByCountryIdOrderByName(@Param("countryId") Long countryId);
}
