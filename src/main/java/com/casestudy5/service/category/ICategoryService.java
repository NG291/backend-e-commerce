package com.casestudy5.service.category;

import com.casestudy5.model.entity.product.Category;
import com.casestudy5.service.IGenerateService;

import java.util.Optional;

public interface ICategoryService extends IGenerateService<Category> {
 Category findByName(String name);
}
