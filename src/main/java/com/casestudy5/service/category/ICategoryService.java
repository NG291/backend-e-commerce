package com.casestudy5.service.category;

import com.casestudy5.model.entity.category.Category;
import com.casestudy5.service.IGenerateService;

public interface ICategoryService extends IGenerateService<Category> {
 Category findByName(String name);
 
}
