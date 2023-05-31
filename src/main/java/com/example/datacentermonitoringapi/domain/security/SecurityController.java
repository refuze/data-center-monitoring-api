package com.example.datacentermonitoringapi.domain.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/security")
@RequiredArgsConstructor
public class SecurityController {
    private final UserService userService;


    @GetMapping
    public ResponseEntity<UserResponse> getPrincipal() {
        User principal = userService.findPrincipal();
        UserResponse principalResponse = UserMapper.toResponse(principal);
        return ResponseEntity.ok(principalResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PatchMapping("/email")
    public ResponseEntity<Void> patchEmail(@RequestBody String email) {
        userService.changeEmail(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/registration")
    public ResponseEntity<Void> registration(@RequestBody String email) {
        userService.register(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.authenticate(loginRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
