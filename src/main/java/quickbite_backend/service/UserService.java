
package quickbite_backend.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import quickbite_backend.model.User;
import quickbite_backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(
            String firstName,
            String lastName,
            String email,
            String password) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email already registered");
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        // Store the encoded password, not the plain password.
        user.setPasswordHash(passwordEncoder.encode(password));

        return userRepository.save(user);
    }
    public User loginUser(String email, String password) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException(
                    "Invalid email or password");
        }

        String storedPassword = user.getPasswordHash();

        // Check whether password is already BCrypt encoded
        if (storedPassword != null &&
                storedPassword.matches("^\\$2[aby]\\$.{56}$")) {

            if (passwordEncoder.matches(password, storedPassword)) {
                return user;
            }

        } else {

            // Migrate old plain-text password to BCrypt
            if (password.equals(storedPassword)) {

                user.setPasswordHash(
                        passwordEncoder.encode(password));

                userRepository.save(user);

                return user;
            }
        }

        throw new IllegalArgumentException(
                "Invalid email or password");
    }
}
