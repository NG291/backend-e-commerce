package com.casestudy5.controller.admin;

import com.casestudy5.model.entity.user.SellerRequest;
import com.casestudy5.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private IUserService userService;

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
}
