package com.casestudy5.repo;

import com.casestudy5.model.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);;
}
