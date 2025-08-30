package com.ijaa.user.domain.entity;

 import com.ijaa.user.domain.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String userId; // This is your unique identifier

    @Column(unique = true, nullable = false, length = 100)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;

    // Google OAuth fields
    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(length = 255)
    private String profilePictureUrl;

    @Column(length = 100)
    private String googleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AuthProvider authProvider = AuthProvider.LOCAL;

    @Column(length = 100)
    private String locale;

    @Column(length = 10)
    private String emailVerified;

    // Add other fields as needed (email, firstName, lastName, etc.)
}
