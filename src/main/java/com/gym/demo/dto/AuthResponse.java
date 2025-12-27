package com.gym.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private
    @Builder.Default
    private String tokenType = "Bearer";

    // Convenience constructor used by AuthService
    public AuthResponse(String token) {
        this.token = token;
        this.tokenType = "Bearer";
    }
}
