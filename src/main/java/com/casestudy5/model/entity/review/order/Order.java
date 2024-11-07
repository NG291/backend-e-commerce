package com.casestudy5.model.entity.review.order;

import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.user.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderedAt = new Date();

    private boolean isPaid = false;

}

