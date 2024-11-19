package com.casestudy5.model.entity.cart;

import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
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
    @JoinColumn(name = "product_id")
    private Product product;
    private OrderStatus status;
    private double totalAmount;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;
}

