
package quickbite_backend.controller;

import java.util.Map;

import jakarta.validation.Valid;
import quickbite_backend.security.JwtService;
import quickbite_backend.service.RefreshTokenService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import quickbite_backend.model.LoginRequest;
import quickbite_backend.model.SignupRequest;
import quickbite_backend.model.User;
import quickbite_backend.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5174")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public UserController(
            UserService userService,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {

        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody SignupRequest request) {

        try {
            User user = userService.registerUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Signup successful",
                            "userId", user.getUserId(),
                            "email", user.getEmail()
                    ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        try {
            User user = userService.loginUser(
                    request.getEmail(),
                    request.getPassword()
            );

            String accessToken = jwtService.generateAccessToken(
                    user.getUserId(),
                    user.getEmail(),
                    user.getRole()
            );

            var refreshToken =
                    refreshTokenService.createRefreshToken(
                            user.getUserId()
                    );

            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "token", accessToken,
                    "refreshToken", refreshToken.getToken(),
                    "userId", user.getUserId(),
                    "firstName", user.getFirstName(),
                    "lastName", user.getLastName(),
                    "email", user.getEmail()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
