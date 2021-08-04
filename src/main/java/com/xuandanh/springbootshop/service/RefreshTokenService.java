package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.RefreshToken;
import com.xuandanh.springbootshop.exception.TokenRefreshException;
import com.xuandanh.springbootshop.repository.RefreshTokenRepository;
import com.xuandanh.springbootshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${bezkoder.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    public RefreshTokenService( RefreshTokenRepository refreshTokenRepository,UserRepository userRepository){
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Long getRefreshTokenDurationMs() {
        return refreshTokenDurationMs;
    }

    public RefreshTokenRepository getRefreshTokenRepository() {
        return refreshTokenRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        if(userRepository.findById(userId).isPresent()){
            refreshToken.setUser(userRepository.findById(userId).get());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());

            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }
        return new RefreshToken();
    }


    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        if(userRepository.findById(userId).isPresent()){
            return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
        }
       return 0;
    }
}
