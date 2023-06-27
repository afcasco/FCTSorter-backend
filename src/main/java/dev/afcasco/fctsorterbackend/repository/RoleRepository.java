package dev.afcasco.fctsorterbackend.repository;

import dev.afcasco.fctsorterbackend.entity.ERole;
import dev.afcasco.fctsorterbackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
