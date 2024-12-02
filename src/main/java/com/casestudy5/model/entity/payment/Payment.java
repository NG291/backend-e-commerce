package com.casestudy5.model.entity.payment;

import com.casestudy5.model.entity.order.Order;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private LocalDateTime paymentDate = LocalDateTime.now();

    private double paymentAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethodStatus paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}