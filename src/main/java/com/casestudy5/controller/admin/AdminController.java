package com.casestudy5.controller.admin;

import com.casestudy5.model.entity.user.SellerRequest;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.service.user.IUserService;
import com.casestudy5.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/seller-requests")
    public List<SellerRequest> getSellerRequests() {
        return userService.getAllSellerRequests();
    }

    @PostMapping("/approve-seller/{userId}")
    public String approveSeller(@PathVariable Long userId) {
        return userService.approveSeller(userId);
    }

    @PostMapping("/reject-seller/{userId}")
    public String rejectSeller(@PathVariable Long userId) {
        return userService.rejectSeller(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam("searchTerm") String searchTerm) {
        List<User> users = userService.searchNameOrUsername(searchTerm);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
