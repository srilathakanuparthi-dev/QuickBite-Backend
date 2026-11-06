
package quickbite_backend.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import quickbite_backend.model.RefreshToken;
import quickbite_backend.repository.RefreshTokenRepository;
import quickbite_backend.security.JwtService;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public RefreshToken createRefreshToken(Integer userId) {

        refreshTokenRepository.deleteAllByUserId(userId);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUserId(userId);
        refreshToken.setToken(jwtService.generateRefreshToken());
        refreshToken.setLastUsedAt(Instant.now());
        refreshToken.setExpiresAt(
                Instant.now().plus(30, ChronoUnit.DAYS)
        );

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken verifyAndRefresh(String token) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Session expired. Please login again.")
                );

        Instant now = Instant.now();

        if (!refreshToken.getExpiresAt().isAfter(now)) {
            refreshTokenRepository.delete(refreshToken);

            throw new RuntimeException(
                    "Session expired. Please login again."
            );
        }

        refreshToken.setLastUsedAt(now);
        refreshToken.setExpiresAt(
                now.plus(30, ChronoUnit.DAYS)
        );

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
