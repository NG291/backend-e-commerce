package com.casestudy5.service.product;

import com.casestudy5.model.entity.image.Image;
import com.casestudy5.model.entity.image.ImageDTO;
import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.product.ProductDTO;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.repo.IImageRepository;
import com.casestudy5.repo.IProductRepository;
import com.casestudy5.repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IImageRepository imageRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    @Transactional
    public Product addProduct(ProductDTO productDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setUser(user);  // Gán seller cho sản phẩm
        product.setCategory(productDTO.getCategory()); // Gán category cho sản phẩm

        product = productRepository.save(product);

        for (ImageDTO imageDTO : productDTO.getImages()) {
            Image image = new Image();
            image.setImageType(imageDTO.getImageType());
            image.setFileName(imageDTO.getFileName());
            image.setProduct(product);
            imageRepository.save(image);
        }
        return product;
    }


}
