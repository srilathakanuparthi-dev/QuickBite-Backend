package quickbite_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import quickbite_backend.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    User findByEmail(String email);
}
