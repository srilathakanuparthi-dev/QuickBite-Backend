
package quickbite_backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import quickbite_backend.model.RefreshToken;
import quickbite_backend.model.User;
import quickbite_backend.repository.UserRepository;
import quickbite_backend.security.JwtService;
import quickbite_backend.service.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5174")
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(
            RefreshTokenService refreshTokenService,
            JwtService jwtService,
            UserRepository userRepository) {

        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestBody Map<String, String> request) {

        try {
            String oldRefreshToken = request.get("refreshToken");

            if (oldRefreshToken == null || oldRefreshToken.isBlank()) {
                return ResponseEntity.status(401)
                        .body(Map.of("message", "Refresh token missing"));
            }

            RefreshToken refreshToken =
                    refreshTokenService.verifyAndRefresh(oldRefreshToken);

            User user = userRepository.findById(
                    refreshToken.getUserId()
            ).orElseThrow();

            String newAccessToken = jwtService.generateAccessToken(
                    user.getUserId(),
                    user.getEmail(),
                    user.getRole()
            );

            return ResponseEntity.ok(Map.of(
                    "token", newAccessToken,
                    "refreshToken", refreshToken.getToken()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Session expired. Please login again."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");

        if (refreshToken != null && !refreshToken.isBlank()) {
            refreshTokenService.deleteRefreshToken(refreshToken);
        }

        return ResponseEntity.ok(
                Map.of("message", "Logged out successfully")
        );
    }
}
