package dev.afcasco.fctsorterbackend.repository;

import dev.afcasco.fctsorterbackend.model.RefreshToken;
import dev.afcasco.fctsorterbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    // TODO check whether this brakes something
  /*  @Modifying
    int deleteByUser(User user);*/

    void deleteByUser(User user);

    RefreshToken findByUser(User user);
}