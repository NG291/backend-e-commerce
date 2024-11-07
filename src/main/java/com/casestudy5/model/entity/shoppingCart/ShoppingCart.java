package com.casestudy5.model.entity.shoppingCart;


import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;

    // Getters and Setters
}

