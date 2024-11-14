package com.casestudy5.model.entity.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Data
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private Set<RoleName> roles;
}
