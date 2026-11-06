
package quickbite_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import quickbite_backend.model.RefreshToken;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    void deleteAllByUserId(Integer userId);
}
