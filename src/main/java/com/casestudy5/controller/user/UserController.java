package com.casestudy5.controller.user;


import com.casestudy5.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;
    @PostMapping("/request-seller-role")
    public String requestSellerRole(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username không được bỏ trống.");
        }
        return userService.requestSellerRole(username);
    }
}