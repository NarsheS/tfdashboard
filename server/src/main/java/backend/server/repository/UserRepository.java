package backend.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.server.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
