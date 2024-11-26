package com.casestudy5.repo;

import com.casestudy5.model.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserId(Long userId);

    List<Product> findByCategoryName(String categoryName);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT p FROM Product p ORDER BY p.price ASC")
    List<Product> findAllByOrderByPriceAsc();

    @Query("SELECT p FROM Product p ORDER BY p.price DESC")
    List<Product> findAllByOrderByPriceDesc();
}