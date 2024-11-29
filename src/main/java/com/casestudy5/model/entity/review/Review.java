package com.casestudy5.model.entity.review;

import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reviews")
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "Rating must be between 1 and 5")
    @NotNull(message = "Rating is required")
    private Integer rating;

    private String comment;

    @ManyToOne

    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne

    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}