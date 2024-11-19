package com.casestudy5.model.entity.user;


import com.casestudy5.model.entity.cart.CartItem;
import com.casestudy5.model.entity.cart.Order;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
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
    private String phoneNumber;
    private String address;
    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<CartItem> cartItems;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


}
