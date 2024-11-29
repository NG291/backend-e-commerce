package com.casestudy5.service.user;

import com.casestudy5.model.entity.user.User;
import com.casestudy5.repo.IUserRepository;
import com.casestudy5.service.email.EmailService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@EnableAsync
@Service
public class PasswordResetService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Async
    public void resetPassword(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user = optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Generate a random default password
        String defaultPassword = generateRandomPassword(12); // Length of 12 characters
        user.setPassword(passwordEncoder.encode(defaultPassword));
        userRepository.save(user);

        // Send email with the new password
        String subject = "Password Reset Request";
        String body = "Your new password is: " + defaultPassword;
        emailService.sendEmail(email, subject, body);
    }

    // Utility method to generate a random password
    private String generateRandomPassword(int length) {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}
