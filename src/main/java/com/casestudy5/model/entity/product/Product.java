package com.casestudy5.model.entity.product;

import com.casestudy5.model.entity.review.order.Order;
import com.casestudy5.model.entity.review.Review;
import com.casestudy5.model.entity.store.Store;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private String name;
    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private boolean isActive = true;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

    @OneToMany(mappedBy = "product")
    private List<Order> orders;

}
