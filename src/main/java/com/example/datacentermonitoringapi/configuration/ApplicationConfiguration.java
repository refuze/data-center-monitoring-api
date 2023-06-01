package com.example.datacentermonitoringapi.configuration;

import com.example.datacentermonitoringapi.configuration.exception.HttpRuntimeException;
import com.example.datacentermonitoringapi.domain.security.UserRepository;
import com.example.datacentermonitoringapi.domain.sensor.SensorRepository;
import com.example.datacentermonitoringapi.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final UserRepository userRepository;
    private final SensorRepository sensorRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if (username.equals("admin")) {
                return userRepository.findAdmin().orElseThrow(() -> new HttpRuntimeException("Admin not found", HttpStatus.NOT_FOUND));
            }

            long id = Utils.extractIdFromUsername(username);

            if (username.startsWith("user_")) {
                return userRepository.findById(id).orElseThrow(() -> new HttpRuntimeException("User not found", HttpStatus.NOT_FOUND));
            } else if (username.startsWith("sensor_")) {
                return sensorRepository.findById(id).orElseThrow(() -> new HttpRuntimeException("Sensor not found", HttpStatus.NOT_FOUND));
            }

            throw new HttpRuntimeException("Incorrect username format", HttpStatus.BAD_REQUEST);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return  config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
