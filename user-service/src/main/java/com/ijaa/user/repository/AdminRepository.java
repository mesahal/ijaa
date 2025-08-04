package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.Admin;
import com.ijaa.user.domain.enums.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);
    
    Optional<Admin> findByEmailAndActiveTrue(String email);
    
    boolean existsByEmail(String email);
    
    List<Admin> findByRole(AdminRole role);
    
    List<Admin> findByActiveTrue();
    
    @Query("SELECT COUNT(a) FROM Admin a WHERE a.role = ?1")
    Long countByRole(AdminRole role);
    
    @Query("SELECT COUNT(a) FROM Admin a WHERE a.active = true")
    Long countActiveAdmins();
} 