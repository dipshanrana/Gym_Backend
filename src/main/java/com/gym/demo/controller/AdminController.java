package com.gym.demo.controller;

import com.gym.demo.model.User;
import com.gym.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStats() {
        long totalUsers = userRepository.count();
        long adminCount = userRepository.findAll().stream()
                .filter(u -> "ROLE_ADMIN".equals(u.getRole()))
                .count();
        long userCount = userRepository.findAll().stream()
                .filter(u -> "ROLE_USER".equals(u.getRole()))
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("adminUsers", adminCount);
        stats.put("regularUsers", userCount);

        return ResponseEntity.ok(stats);
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestBody Map<String, String> request) {

        String username = request.get("username");
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Current password is incorrect"));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }
}

