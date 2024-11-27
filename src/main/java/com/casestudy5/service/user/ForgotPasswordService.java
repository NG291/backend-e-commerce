package com.casestudy5.service.user;

import com.casestudy5.model.entity.user.User;
import com.casestudy5.repo.IUserRepository;
import com.casestudy5.service.email.EmailService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@EnableAsync
@Service
public class ForgotPasswordService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private IUserRepository userRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Async
    public void sendResetLink(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new EntityNotFoundException("User not found.");
        }

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setTokenExpiry(LocalDateTime.now().plusMinutes(15)); // Token expires in 15 minutes
        userRepository.save(user);

        String resetLink = frontendUrl + "/reset-password?token=" + resetToken;
        String subject = "Reset Your Password";
        String body = "Click the link to reset your password: " + resetLink;
        emailService.sendEmail(email, subject, body);
    }

    public void resetPasswordWithToken(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Invalid or expired reset token."));

        if (user.getTokenExpiry() == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired token.");
        }

        // Hash the new password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10); // 10 rounds
        String hashedPassword = passwordEncoder.encode(newPassword);

        // Update user password and reset token fields
        user.setPassword(hashedPassword);
        user.setResetToken(null);
        user.setTokenExpiry(null);
        userRepository.save(user);
    }
}