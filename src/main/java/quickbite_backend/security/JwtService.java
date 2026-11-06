
package quickbite_backend.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey secretKey = Keys.hmacShaKeyFor(
        "QuickBiteSecretKeyForJwtAuthentication2026Secure".getBytes()
    );

    // Access token: 15 minutes
    private final long accessExpiration = 1000L * 60 * 15;

    public String generateAccessToken(
            Integer userId,
            String email,
            String role) {

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(
                    System.currentTimeMillis() + accessExpiration
                ))
                .signWith(secretKey)
                .compact();
    }

    // Refresh token: random secure token
    public String generateRefreshToken() {
        return java.util.UUID.randomUUID().toString()
                + java.util.UUID.randomUUID().toString();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public String extractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
