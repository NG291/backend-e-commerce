package com.casestudy5.service.user;

import com.casestudy5.model.entity.user.User;
import com.casestudy5.service.IGenerateService;

public interface IUserService extends IGenerateService<User> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByName(String name);
}
