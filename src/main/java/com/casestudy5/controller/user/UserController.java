package com.casestudy5.controller.user;


import com.casestudy5.model.entity.user.User;
import com.casestudy5.service.user.IUserService;
import com.casestudy5.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    private UserService userService;

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
}