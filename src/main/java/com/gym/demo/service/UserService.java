package com.gym.demo.service;

import com.gym.demo.dto.ChangePasswordDto;
import com.gym.demo.exception.BadRequestException;
import com.gym.demo.model.User;
import com.gym.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void changePassword(ChangePasswordDto dto) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Find user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        // Verify new passwords match
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("New passwords do not match");
        }

        // Verify new password is different from current
        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("New password must be different from current password");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        // Mark password as changed (no longer require password change)
        user.setPasswordChangeRequired(false);

        userRepository.save(user);
    }

    @Transactional
    public void forcePasswordChange(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found"));

        user.setPasswordChangeRequired(true);
        userRepository.save(user);
    }
}

