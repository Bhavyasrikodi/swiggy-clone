package com.swiggy.user.controller;

import com.swiggy.user.entity.User;
import com.swiggy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        String email = authentication.getName();
        log.info("GET /api/users/me - email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", email);
                    return new RuntimeException("User not found");
                });

        user.setPassword(null);
        log.debug("Profile fetched for userId: {}", user.getId());
        return ResponseEntity.ok(user);
    }
}