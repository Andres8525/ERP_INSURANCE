package com.erp.insurance.auth;

import com.erp.insurance.domain.DomainEnums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrap {
    @Bean
    ApplicationRunner bootstrapAdministrator(UserRepository users, PasswordEncoder encoder,
                                             @Value("${app.bootstrap.admin-email:}") String email,
                                             @Value("${app.bootstrap.admin-password:}") String password) {
        return args -> {
            if (email.isBlank() && password.isBlank()) return;
            if (email.isBlank() || password.length() < 16) {
                throw new IllegalStateException("Set both bootstrap admin values and use a password of at least 16 characters");
            }
            if (users.count() == 0) {
                users.save(new UserEntity(email.trim().toLowerCase(), encoder.encode(password), UserRole.ADMIN));
            }
        };
    }
}