package com.casestudy5.model.entity.review;

import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "reviews")
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int rating;
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

}

