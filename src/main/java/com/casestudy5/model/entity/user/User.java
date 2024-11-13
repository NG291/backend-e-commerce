package com.casestudy5.model.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 50)
    @Column(unique = true)
    private String username;

    @Size(min = 8)
    private String password;

    private String name;

    @Email(message = "Vui lòng nhập email hợp lệ.")
    @NotBlank(message = "Email không được để trống.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "Email phải có đuôi @gmail.com")
    @Column(unique = true)
    private String email;
    private LocalDate birthDate;
    @Pattern(regexp = "^(0[0-9]{9})$")
    private String phoneNumber;
    private String address;
    @DecimalMin(value = "0.01", message = "Salary must be greater than 0")
    @DecimalMax(value = "100000000", message = "Salary must be less than 100000000")
    private BigDecimal salary;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public int getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }
}
