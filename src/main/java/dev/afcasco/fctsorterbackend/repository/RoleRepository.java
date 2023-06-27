package dev.afcasco.fctsorterbackend.repository;

import dev.afcasco.fctsorterbackend.entity.ERole;
import dev.afcasco.fctsorterbackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
