package com.casestudy5.controller.user;


import com.casestudy5.model.entity.user.User;
import com.casestudy5.model.entity.user.UserDTO;
import com.casestudy5.service.user.ForgotPasswordService;
import com.casestudy5.service.user.PasswordResetService;
import com.casestudy5.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/request-seller-role")
    public String requestSellerRole(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        return userService.requestSellerRole(username);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam("searchTerm") String searchTerm) {
        List<User> users = userService.searchNameOrUsername(searchTerm);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/{userId}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable Long userId,
            @RequestBody Map<String, String> passwordData) {
        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");

        if (oldPassword == null || newPassword == null) {
            return ResponseEntity.badRequest().body("Old password and new password must be provided");
        }

        try {
            String message = userService.changePassword(userId, oldPassword, newPassword);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/reset")
    public String resetPassword(@RequestParam String email) {
        passwordResetService.resetPassword(email);
        return "Password reset email sent successfully";
    }

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/send-reset-link")
    public ResponseEntity<String> sendResetLink(@RequestParam String email) {
        try {
            forgotPasswordService.sendResetLink(email);
            return ResponseEntity.ok("Reset link sent! We will check and send link if your email exist.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending reset link.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");
        String newPassword = requestBody.get("newPassword");

        if (token == null || newPassword == null) {
            return ResponseEntity.badRequest().body("Token and new password must be provided.");
        }

        try {
            forgotPasswordService.resetPasswordWithToken(token, newPassword);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error resetting password.");
        }
    }
}
