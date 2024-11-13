package com.casestudy5.repo;

import com.casestudy5.model.entity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);;
}
