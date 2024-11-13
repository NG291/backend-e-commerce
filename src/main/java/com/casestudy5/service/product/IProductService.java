package com.casestudy5.service.product;

import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.product.ProductDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@PreAuthorize("hasRole('ROLE_SELLER')")

public interface IProductService {
    Product addProduct(ProductDTO productDTO, Long userId);
}

