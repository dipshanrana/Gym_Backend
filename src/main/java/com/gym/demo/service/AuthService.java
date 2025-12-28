package com.gym.demo.service;

import com.gym.demo.dto.AuthResponse;
import com.gym.demo.dto.LoginDto;
import com.gym.demo.dto.RegisterDto;
import com.gym.demo.dto.UserDto;
import com.gym.demo.exception.BadRequestException;
import com.gym.demo.model.User;
import com.gym.demo.repository.UserRepository;
import com.gym.demo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public void register(RegisterDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Username already taken");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already in use");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role("ROLE_USER")
                .build();
        userRepository.save(user);
    }

    public AuthResponse login(LoginDto dto) {
        // authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsernameOrEmail(), dto.getPassword())
        );
        // find user
        User user = userRepository.findByUsername(dto.getUsernameOrEmail())
                .orElseGet(() -> userRepository.findByEmail(dto.getUsernameOrEmail())
                        .orElseThrow(() -> new BadRequestException("Invalid credentials")));

        // Generate token with role
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        // Create UserDto with user details
        UserDto userDto = new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getPasswordChangeRequired()
        );

        // Return response with nested user object
        return new AuthResponse(token, userDto);
    }
}

