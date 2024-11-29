package com.casestudy5.config.service;

import com.casestudy5.model.entity.user.Role;
import com.casestudy5.model.entity.user.RoleName;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.repo.IRoleRepository;
import com.casestudy5.repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class OAuth2Service {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    // Thay đổi phương thức này trả về Optional<User>
    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);  // Tìm người dùng bằng email
    }

    public User createUser(OAuth2User oAuth2User, String provider) {
        if (oAuth2User == null) {
            throw new IllegalArgumentException("OAuth2User cannot be null");
        }

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String avatar = oAuth2User.getAttribute("picture");

        if (email == null) {
            throw new IllegalArgumentException("Email is required for OAuth2 registration");
        }

        User user = new User();
        user.setUsername(email);
        user.setEmail(email);
        user.setName(name != null ? name : "Unknown");
        user.setAvatar(avatar != null ? avatar : "");

        Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(Set.of(defaultRole));

        return userRepository.save(user);
    }

    // Add the updateUser method
    public User updateUser(OAuth2User oAuth2User, User existingUser) {
        existingUser.setName(oAuth2User.getAttribute("name"));
        existingUser.setEmail(oAuth2User.getAttribute("email"));
        // Add other fields you want to update here, such as profile picture, etc.
        return userRepository.save(existingUser);  // Save updated user to database
    }

}
