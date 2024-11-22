package com.casestudy5.service.user;

import com.casestudy5.model.entity.user.User;
import com.casestudy5.repo.IUserRepository;
import com.casestudy5.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private IUserRepository userRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    public void sendResetLink(String email) {
        User user = userRepository.findByEmail(email);

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setTokenExpiry(LocalDateTime.now().plusMinutes(15)); // Token expires in 15 minutes
        userRepository.save(user);

        String resetLink = frontendUrl + "reset-password?token=" + resetToken; // Adjust to your frontend route
        String subject = "Reset Your Password";
        String body = "Click the link to reset your password: " + resetLink;
        emailService.sendEmail(email, subject, body);
    }

    public void resetPasswordWithEmail(String email, String newPassword) {
        User user = userRepository.findByEmail(email);


        user.setPassword(newPassword); // Ensure to encode it
        userRepository.save(user); // Lưu lại mật khẩu mới
    }
}