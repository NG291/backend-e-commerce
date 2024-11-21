package com.casestudy5.repo;

import com.casestudy5.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByName(String name);

    List<User>findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(String name, String username);

    Optional<User> findByResetToken(String resetToken);
}
