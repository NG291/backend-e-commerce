package com.casestudy5.repo;

import com.casestudy5.model.entity.user.AuthProvider;
import com.casestudy5.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    List<User> findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(String name, String username);

    Optional<User> findByResetToken(String resetToken);

    Optional<User> findByEmailAndAuthProvider(String email, AuthProvider authProvider);
}