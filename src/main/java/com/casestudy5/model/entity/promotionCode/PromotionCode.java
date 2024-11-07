package com.casestudy5.model.entity.promotionCode;


import com.casestudy5.model.entity.store.Store;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "promotion_codes")
public class PromotionCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private String code;

    @Column(precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Temporal(TemporalType.TIMESTAMP)
    private Date validUntil;

    // Getters and Setters
}

