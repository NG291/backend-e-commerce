package com.casestudy5.repo;

import com.casestudy5.model.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductRepository extends JpaRepository<Product, Long> {
}