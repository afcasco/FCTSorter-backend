package dev.afcasco.fctsorterbackend.service;

import dev.afcasco.fctsorterbackend.entity.RefreshToken;
import dev.afcasco.fctsorterbackend.entity.User;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    int deleteByUser(User user);

    RefreshToken save(RefreshToken refreshToken);

    void delete(RefreshToken refreshToken);

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    int deleteByUserId(Long userId);

}
