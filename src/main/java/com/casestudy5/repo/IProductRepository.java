package com.casestudy5.repo;

import com.casestudy5.model.entity.category.Category;
import com.casestudy5.model.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserId(Long userId);

    @Query("SELECT p FROM Product p WHERE p.category.name = :categoryName")
    List<Product> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );

    @Query(value = "SELECT p.id, " +
            "p.name, " +
            "p.description, " +
            "p.price, " +
            "p.quantity, " +
            "p.is_active, " +
            "GROUP_CONCAT(DISTINCT i.file_name) AS images " +
            "FROM products p " +
            "JOIN order_items oi ON p.id = oi.product_id " +
            "LEFT JOIN images i ON p.id = i.product_id " +
            "GROUP BY p.id, p.name, p.description, p.price, p.quantity, p.is_active " +
            "ORDER BY SUM(oi.quantity) DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Object[]> findTopSellingProducts();

}