package dev.afcasco.fctsorterbackend.repository;

import dev.afcasco.fctsorterbackend.model.ERole;
import dev.afcasco.fctsorterbackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}