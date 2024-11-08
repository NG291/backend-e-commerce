package com.casestudy5.service.role;

import com.casestudy5.model.dto.GetNumberOfRole;
import com.casestudy5.model.entity.user.Role;

import java.util.Optional;

public interface IRoleService {
    Iterable<GetNumberOfRole> getAllNumberOfRole();
    Role findByName(String name);
    Iterable<Role> getAllRoles();
    Optional<Role> findById(Long id);
}
