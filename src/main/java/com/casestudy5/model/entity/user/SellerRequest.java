package com.casestudy5.model.entity.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SellerRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private String status;
}
