package com.example.datacentermonitoringapi.domain.security;

import com.example.datacentermonitoringapi.configuration.exception.HttpRuntimeException;
import com.example.datacentermonitoringapi.domain.email.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new HttpRuntimeException("User not found", HttpStatus.NOT_FOUND, null));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new HttpRuntimeException("User not found", HttpStatus.NOT_FOUND, null));
    }

    public User findPrincipal() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByUsername(username);
    }

    public List<UserResponse> findAll() {
        return userRepository.findAllUsers().stream().map(UserMapper::toResponse).toList();
    }

    public List<String> getNotifiableEmails() {
        return userRepository.getNotifiableEmails();
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void changeEmail(String email) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new HttpRuntimeException("Email already exist", HttpStatus.CONFLICT, null);
        }

        userRepository.changeEmailByUsername(email, username);
    }

    @Transactional
    public void register(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            return;
        }

        String username = generateUniqueUsername();
        String password = PasswordUtil.generateSecureRandomPassword(10, 10, 10);

        User newUser = User.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .isNotificationEnabled(true)
                .build();

        userRepository.save(newUser);

        sendRegistrationEmail(username, password, email);
    }

    public String authenticate(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication auth = authManager.authenticate(authReq);
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = findByUsername(loginRequest.getUsername());
        if (user.getRole().equals(Role.SENSOR)) {
            throw new HttpRuntimeException("Sensor can't authenticate", HttpStatus.BAD_REQUEST);
        }
        return jwtService.generateToken(user);
    }

    private String generateUniqueUsername() {
        String username;
        do {
            username = "username_" + ThreadLocalRandom.current().nextInt(100000, 999999);
        } while (userRepository.findByUsername(username).isPresent());
        return username;
    }

    private void sendRegistrationEmail(String username, String password, String email) {
        emailService.sendSimpleEmail(
                email,
                "Registration success",
                """
                 Вы успешно зарегестрированы в системе, вот ваши данные для входа:
                 Имя пользователя: %s
                 Пароль: %s
                 """.formatted(username, password)
        );
    }

}
