package com.casestudy5.model.entity.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Data
@Setter
public class UserDTO {
    private Long id;
    @NotBlank(message = "Username must be filled!")
    @Size(min = 1, max = 50, message = "Username must be from 1-50 characters")
    private String username;
    @NotBlank(message = "Password must be filled!")
    private String password;
    @NotBlank(message = "Email must be filled!")
    @Email(message = "Email must be legit!")
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private Set<RoleName> roles;
}
