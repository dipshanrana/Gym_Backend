package com.gym.demo.config;

import com.gym.demo.model.User;
import com.gym.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DataInitializer creates default admin user on application startup
 * if no admin user exists.
 *
 * Admin credentials can be configured via:
 * 1. application.properties (default)
 * 2. Environment variables (recommended for production)
 *
 * Environment Variables:
 * - ADMIN_USERNAME (default: admin)
 * - ADMIN_EMAIL (default: admin@fitfuel.com)
 * - ADMIN_PASSWORD (default: Admin@123)
 * - ADMIN_FULLNAME (default: System Administrator)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.fullname}")
    private String adminFullName;

    @Value("${admin.enabled:true}")
    private Boolean adminEnabled;

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }

    /**
     * Creates default admin user if it doesn't exist
     */
    private void initializeAdminUser() {
        try {
            // Check if admin user already exists
            if (userRepository.existsByUsername(adminUsername)) {
                log.info("✅ Admin user '{}' already exists", adminUsername);
                return;
            }

            if (userRepository.existsByEmail(adminEmail)) {
                log.info("✅ Admin email '{}' already exists", adminEmail);
                return;
            }

            // Create admin user
            User admin = User.builder()
                    .username(adminUsername)
                    .email(adminEmail)
                    .fullname(adminFullName)
                    .password(passwordEncoder.encode(adminPassword))
                    .role("ROLE_ADMIN")
                    .enabled(adminEnabled)
                    .passwordChangeRequired(true)  // Force password change on first login
                    .build();

            userRepository.save(admin);

            // Log success (avoid logging password)
            log.info("========================================");
            log.info("✅ ADMIN USER CREATED SUCCESSFULLY!");
            log.info("========================================");
            log.info("📧 Email:    {}", adminEmail);
            log.info("👤 Username: {}", adminUsername);
            log.info("🔑 Password: {}", maskPassword(adminPassword));
            log.info("👔 Role:     ROLE_ADMIN");
            log.info("========================================");
            log.warn("⚠️  IMPORTANT: Change the admin password after first login!");
            log.warn("⚠️  For production, use environment variables for credentials!");
            log.info("========================================");

        } catch (Exception e) {
            log.error("❌ Failed to create admin user: {}", e.getMessage());
            // Don't throw exception - let app continue
        }
    }

    /**
     * Masks password for logging (shows first 2 and last 2 characters)
     */
    private String maskPassword(String password) {
        if (password == null || password.length() <= 4) {
            return "****";
        }
        return password.substring(0, 2) + "****" + password.substring(password.length() - 2);
    }
}

