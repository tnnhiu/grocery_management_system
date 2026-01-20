package com.tnnhiu.grocery_management_server.common.configuration;


import com.tnnhiu.grocery_management_server.modules.identity.entity.Role;
import com.tnnhiu.grocery_management_server.modules.identity.entity.User;
import com.tnnhiu.grocery_management_server.modules.identity.enums.RoleEnum;
import com.tnnhiu.grocery_management_server.modules.identity.repository.RoleRepository;
import com.tnnhiu.grocery_management_server.modules.identity.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfiguration {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Bean
    ApplicationRunner init(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            // Init Roles
            if (roleRepository.count() == 0) {
                Arrays.stream(RoleEnum.values()).forEach(roleEnum -> {
                    Role role = Role.builder()
                            .name(roleEnum)
                            .build();
                    roleRepository.save(role);
                    log.info("Role {} has been created", roleEnum);
                });
            }

            // Init Admin User
            if (!userRepository.existsByUsername("admin")) {
                Role adminRole = roleRepository.findByName(RoleEnum.ADMIN)
                        .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

                User adminUser = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .name("Administrator")
                        .role(adminRole)
                        .enabled(true)
                        .build();

                userRepository.save(adminUser);
                log.info("Admin user has been created with username: admin, password: admin123");
            }
        };
    }
}
