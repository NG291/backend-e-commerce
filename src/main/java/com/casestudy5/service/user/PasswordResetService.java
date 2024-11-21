package com.casestudy5.service.user;

import com.casestudy5.model.entity.user.User;
import com.casestudy5.repo.IUserRepository;
import com.casestudy5.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private IUserRepository userRepository; // Replace with your user repository

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email);

        String defaultPassword = "1234"; // Replace with any generated default password logic
        user.setPassword(passwordEncoder.encode(defaultPassword));
        userRepository.save(user);

        String subject = "Password Reset Request";
        String body = "Your new password is: " + defaultPassword;
        emailService.sendEmail(email, subject, body);
    }
}