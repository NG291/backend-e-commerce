package com.casestudy5.model.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(unique = true)
    private String username;

    @NotNull
    @Size(min = 8)
    private String password;

    @NotNull
    private String name;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;
    @NotNull
    @Min(19)
    @Max(59)
    private int age;

    @Pattern(regexp = "^(0[0-9]{9})$")
    private String phoneNumber;
    private String address;

    @NotNull
    @DecimalMin(value = "0.01", message = "Salary must be greater than 0")
    @DecimalMax(value = "100000000", message = "Salary must be less than 100000000")
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    private UserRole role;


}
