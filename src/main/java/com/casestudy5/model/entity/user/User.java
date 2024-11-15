package com.casestudy5.model.entity.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
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

    @Size(min = 4)
    private String password;

    private String name;

    @Email(message = "Email must be valid!")
    @NotBlank(message = "Email cannot be blank!")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "Must be @gmail.com")
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
    private Set<Role> roles = new HashSet<>();

    public Integer getAge() {
        if (this.birthDate == null) {
            return null; // Or return a default value if appropriate, like 0
        }
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

}
