package com.casestudy5.model.entity.product;

import com.casestudy5.model.entity.image.Image;
import com.casestudy5.model.entity.review.order.Order;
import com.casestudy5.model.entity.review.Review;

import com.casestudy5.model.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private boolean isActive = true;


    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

    @OneToMany(mappedBy = "product")
    private List<Order> orders;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @JsonBackReference
    @Size(max = 4, message = "Toi da 4 anh.")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    private List<Image> images = new ArrayList<>();

}
