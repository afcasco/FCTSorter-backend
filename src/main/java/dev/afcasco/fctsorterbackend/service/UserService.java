package dev.afcasco.fctsorterbackend.service;

import dev.afcasco.fctsorterbackend.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    List<User> findAll();
}
