package com.example.datacentermonitoringapi.domain.security;

import com.example.datacentermonitoringapi.configuration.exception.HttpRuntimeException;
import com.example.datacentermonitoringapi.domain.email.EmailService;
import com.example.datacentermonitoringapi.util.Utils;
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

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new HttpRuntimeException("User not found", HttpStatus.NOT_FOUND));
    }

    public User findByUsername(String username) {
        if (username.equals("admin")) {
            return findAdmin().orElseThrow(() -> new HttpRuntimeException("Admin not found", HttpStatus.NOT_FOUND));
        }
        long id = extractIdFromUsername(username);
        return findById(id);
    }

    public Optional<User> findAdmin() {
        return userRepository.findAdmin();
    }

    private long extractIdFromUsername(String username) {
        if (!username.startsWith("user_")) {
            throw new HttpRuntimeException("Incorrect username format", HttpStatus.BAD_REQUEST);
        }

        return Utils.extractIdFromUsername(username);
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
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void changeEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new HttpRuntimeException("Email already exist", HttpStatus.CONFLICT, null);
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        long id = extractIdFromUsername(username);
        userRepository.changeEmailById(email, id);
    }

    @Transactional
    public void register(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        User user = userOpt.orElseGet(User::new);

        String password = PasswordUtil.generateSecureRandomPassword(3, 3, 4);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(Role.USER);
        user.setNotificationEnabled(true);

        User saveduser = userRepository.save(user);
        emailService.sendRegistrationEmail(saveduser.getUsername(), password, email);
    }

    public String authenticate(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication auth = authManager.authenticate(authReq);
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = findByUsername(loginRequest.getUsername());
        return jwtService.generateToken(user);
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void changeAdminPassword(String password) {
        userRepository.updateAdminPassword(passwordEncoder.encode(password));
    }
}
