package com.casestudy5.service.user;

import com.casestudy5.model.entity.user.SellerRequest;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.service.IGenerateService;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IGenerateService<User> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByName(String name);

    String requestSellerRole(String username);

    String approveSeller(Long userId);

    String rejectSeller(Long userId);

    List<SellerRequest> getAllSellerRequests();

    User findByRole(String username);

    List<User> searchUsers(String searchTerm);

    List<User> searchNameOrUsername(String searchName);

}
