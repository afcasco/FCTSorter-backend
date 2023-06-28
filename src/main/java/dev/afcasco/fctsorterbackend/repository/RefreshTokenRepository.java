package dev.afcasco.fctsorterbackend.repository;

import dev.afcasco.fctsorterbackend.entity.RefreshToken;
import dev.afcasco.fctsorterbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    // TODO check whether this brakes something
  /*  @Modifying
    int deleteByUser(User user);*/

    void deleteByUser(User user);
}