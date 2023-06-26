package dev.afcasco.fctsorterbackend.service;

import dev.afcasco.fctsorterbackend.entity.ERole;
import dev.afcasco.fctsorterbackend.entity.Role;

import java.util.Optional;

public interface RoleService {

    Optional<Role> findByName(ERole name);
}
