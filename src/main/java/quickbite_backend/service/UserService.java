
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

        if (user == null ||
            !passwordEncoder.matches(password, user.getPasswordHash())) {

            throw new IllegalArgumentException(
                "Invalid email or password"
            );
        }

        return user;
    }
}
