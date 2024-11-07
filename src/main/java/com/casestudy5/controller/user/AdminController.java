package com.casestudy5.controller.user;


import com.casestudy5.config.service.JwtService;
import com.casestudy5.model.entity.user.Role;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.service.role.IRoleService;
import com.casestudy5.service.role.RoleService;
import com.casestudy5.service.user.IUserService;
import com.casestudy5.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> existingUser = userService.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();


            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());

            if (updatedUser.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Encode new password
            }

            if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
                Set<Role> roles = new HashSet<>();
                for (Role role : updatedUser.getRoles()) {
                    Role existingRole = roleService.findByName(role.getName().toString());
                    if (existingRole != null) {
                        roles.add(existingRole);
                    }
                }
                user.setRoles(roles);
            }

            userService.save(user);
            return new ResponseEntity<>("User updated successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = userService.findById(id);
        if (existingUser.isPresent()) {
            userService.remove(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
