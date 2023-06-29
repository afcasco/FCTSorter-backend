package dev.afcasco.fctsorterbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.*;


import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NonNull
    @Size(max = 20)
    private String username;

    @NotBlank
    @NonNull
    @Size(max = 50)
    private String email;

    @NotBlank
    @NonNull
    @Size(max = 120)
    private String password;

    @NonNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}