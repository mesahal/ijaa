package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    
    Optional<Country> findByIso2(String iso2);
    
    Optional<Country> findByIso3(String iso3);
    
    Optional<Country> findByName(String name);
    
    @Query("SELECT c FROM Country c WHERE c.flag = 1 ORDER BY c.name ASC")
    List<Country> findAllActiveOrderByName();
    
    @Query("SELECT c FROM Country c WHERE c.name LIKE %:searchTerm% AND c.flag = 1 ORDER BY c.name ASC")
    List<Country> findByNameContainingIgnoreCase(String searchTerm);
}
