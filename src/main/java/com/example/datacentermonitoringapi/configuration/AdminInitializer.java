package com.example.datacentermonitoringapi.configuration;

import com.example.datacentermonitoringapi.domain.security.Role;
import com.example.datacentermonitoringapi.domain.security.User;
import com.example.datacentermonitoringapi.domain.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {
        createAdminUserIfNotExists();
    }

    private void createAdminUserIfNotExists() {
        Optional<User> adminUser = userService.findAdmin();

        if (adminUser.isEmpty()) {
            User user = User.builder()
                    .password(passwordEncoder.encode("admin"))
                    .email("")
                    .isNotificationEnabled(false)
                    .role(Role.ADMIN)
                    .build();

            userService.save(user);
        }
    }
}
