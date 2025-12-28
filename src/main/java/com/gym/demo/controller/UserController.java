package com.gym.demo.controller;

import com.gym.demo.dto.ChangePasswordDto;
import com.gym.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Change password endpoint (authenticated users only)
     */
    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        userService.changePassword(dto);
        return ResponseEntity.ok(Map.of(
                "message", "Password changed successfully",
                "success", true
        ));
    }

    /**
     * Force password change for a user (admin only)
     */
    @PostMapping("/force-password-change/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> forcePasswordChange(@PathVariable String username) {
        userService.forcePasswordChange(username);
        return ResponseEntity.ok(Map.of(
                "message", "User will be required to change password on next login",
                "success", true
        ));
    }
}

