package com.ijaa.user.common.config;

import com.ijaa.user.domain.entity.Admin;
import com.ijaa.user.domain.enums.AdminRole;
import com.ijaa.user.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(1) // Run before TestDataSeeder
public class AdminDataSeeder implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create ADMIN if no admin exists
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setName("Administrator");
            admin.setEmail("admin@ijaa.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(AdminRole.ADMIN);
            admin.setActive(true);

            adminRepository.save(admin);
            log.info("Created default ADMIN with email: admin@ijaa.com and password: admin123");
        } else {
            log.info("Admin already exists, skipping default admin creation");
        }
    }
} 