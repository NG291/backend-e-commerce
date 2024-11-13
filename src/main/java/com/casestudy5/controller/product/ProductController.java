package com.casestudy5.controller.product;

import com.casestudy5.config.UserPrinciple;
import com.casestudy5.model.entity.image.ImageDTO;
import com.casestudy5.model.entity.product.Category;
import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.product.ProductDTO;
import com.casestudy5.service.category.ICategoryService;
import com.casestudy5.service.product.IProductService;
import com.casestudy5.service.product.ImageService;
import com.casestudy5.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ProductService productServices;

    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(@RequestParam("name") String name,
                                              @RequestParam("description") String description,
                                              @RequestParam("price") BigDecimal price,
                                              @RequestParam("category") String category,
                                              @RequestParam("images") List<MultipartFile> images,
                                              Authentication authentication) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Category categoryObj = categoryService.findByName(category);


        List<ImageDTO> imageDTOs = new ArrayList<>();
        if (images != null) {
            for (MultipartFile imageFile : images) {
                ImageDTO imageDTO = imageService.createImage(convertMultipartFileToImageDTO(imageFile));
                imageDTOs.add(imageDTO);
            }
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setDescription(description);
        productDTO.setPrice(price);
        productDTO.setCategory(categoryObj);
        productDTO.setImages(imageDTOs);


        Product product = productService.addProduct(productDTO, userPrinciple.getId());

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    private ImageDTO convertMultipartFileToImageDTO(MultipartFile imageFile) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setFileName(imageFile.getOriginalFilename());
        imageDTO.setImageType(imageFile.getContentType());
        return imageDTO;
    }
}

