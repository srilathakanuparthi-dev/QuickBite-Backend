
package quickbite_backend.controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quickbite_backend.model.LoginRequest;

import quickbite_backend.model.SignupRequest;
import quickbite_backend.model.User;
import quickbite_backend.service.UserService;

@CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody SignupRequest request) {

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
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            User user = userService.loginUser(
                request.getEmail(),
                request.getPassword()
            );

            return ResponseEntity.ok(
                Map.of(
                    "message", "Login successful",
                    "userId", user.getUserId(),
                    "firstName", user.getFirstName(),
                    "lastName", user.getLastName(),
                    "email", user.getEmail()
                )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", e.getMessage()));
        }
    } 
}
