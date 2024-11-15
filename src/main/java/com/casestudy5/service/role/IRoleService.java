package com.casestudy5.service.role;

import com.casestudy5.model.entity.user.Role;
import com.casestudy5.model.entity.user.RoleName;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(RoleName name); // Change parameter type to RoleName
    Iterable<Role> getAllRoles();
    Optional<Role> findById(Long id);
}
