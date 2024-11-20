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
    @NotBlank(message = "Tên người dùng không được để trống")
    @Size(min = 1, max = 50, message = "Tên người dùng phải có độ dài từ 1 đến 50 ký tự")
    private String username;
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email phải hợp lệ")
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private Set<RoleName> roles;
}
