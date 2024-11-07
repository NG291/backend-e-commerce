package com.casestudy5.model.entity.store;

import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.promotionCode.PromotionCode;
import com.casestudy5.model.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "stores")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User user;
    @OneToMany(mappedBy = "store")
    private List<Product> products;

    @OneToMany(mappedBy = "store")
    private List<PromotionCode> promotionCodes;


}
