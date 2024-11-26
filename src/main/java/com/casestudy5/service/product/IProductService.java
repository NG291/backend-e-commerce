package com.casestudy5.service.product;

import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.product.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;


public interface IProductService {
    Product addProduct(ProductDTO productDTO, Long userId);

    Product updateProduct(Long productId, ProductDTO productDTO);

    boolean deleteProduct(Long productId);

    ProductDTO getProductById(Long productId);

    List<ProductDTO> getAllProducts();

}

