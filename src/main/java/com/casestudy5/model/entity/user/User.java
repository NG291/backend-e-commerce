package com.casestudy5.model.entity.user;


import com.casestudy5.model.entity.cart.CartItem;
import com.casestudy5.model.entity.order.Order;
import com.casestudy5.model.entity.notification.Notification;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private String resetToken;
    private LocalDateTime tokenExpiry;

    @JsonIgnoreProperties({"user"})
    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @JsonProperty
    private String avatar;
    @Nullable
    private LocalDate birthDate;

    public Integer getAge(){
        if(birthDate== null){
            return  null;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    public String getFormattedBirthDate(){
        if(birthDate== null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd//MM//yyyy");
        return birthDate.format(formatter);
    }
}
