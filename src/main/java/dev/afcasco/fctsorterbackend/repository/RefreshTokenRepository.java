package dev.afcasco.fctsorterbackend.repository;

import dev.afcasco.fctsorterbackend.entity.RefreshToken;
import dev.afcasco.fctsorterbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByToken(String token);

    int deleteByUser(User user);


}
