package com.gym.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String fullname;

    @Builder.Default
    private Boolean enabled = true;

    @Builder.Default
    @Column(name = "password_change_required")
    private Boolean passwordChangeRequired = false;

    // For simplicity keep a single role string
    @Builder.Default
    private String role = "ROLE_USER";
}
