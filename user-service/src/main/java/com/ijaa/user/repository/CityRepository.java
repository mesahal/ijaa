package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    
    @Query("SELECT c FROM City c WHERE c.countryId = :countryId AND c.flag = 1 ORDER BY c.name ASC")
    List<City> findByCountryIdOrderByName(@Param("countryId") Long countryId);
    
    @Query("SELECT c FROM City c WHERE c.countryId = :countryId AND c.name LIKE %:searchTerm% AND c.flag = 1 ORDER BY c.name ASC")
    List<City> findByCountryIdAndNameContainingIgnoreCase(@Param("countryId") Long countryId, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT c FROM City c WHERE c.name LIKE %:searchTerm% AND c.flag = 1 ORDER BY c.name ASC")
    List<City> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);
    
    Optional<City> findByNameAndCountryId(String name, Long countryId);
}
