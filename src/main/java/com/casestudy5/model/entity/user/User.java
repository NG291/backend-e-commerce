package com.casestudy5.model.entity.user;

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
    private LocalDate birthDate;
    @Pattern(regexp = "^(0[0-9]{9})$")
    private String phoneNumber;
    private String address;

    @NotNull
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
