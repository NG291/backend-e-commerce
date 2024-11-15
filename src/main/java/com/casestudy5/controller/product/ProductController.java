package com.casestudy5.controller.product;

import com.casestudy5.config.UserPrinciple;
import com.casestudy5.model.entity.image.ImageDTO;
import com.casestudy5.model.entity.product.Category;
import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.product.ProductDTO;
import com.casestudy5.service.category.ICategoryService;
import com.casestudy5.service.product.ImageService;
import com.casestudy5.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ProductService productServices;

    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(@RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("price") BigDecimal price, @RequestParam("quantity") int quantity, @RequestParam("category") String category, @RequestParam("images") List<MultipartFile> images, Authentication authentication) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Category categoryObj = categoryService.findByName(category);
        List<ImageDTO> imageDTOs = new ArrayList<>();
        if (images != null) {
            for (MultipartFile imageFile : images) {
                ImageDTO imageDTO = convertMultipartFileToImageDTO(imageFile);
                imageDTOs.add(imageDTO);
            }
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setDescription(description);
        productDTO.setPrice(price);
        productDTO.setQuantity(quantity);
        productDTO.setCategory(categoryObj);
        productDTO.setImages(imageDTOs);


        Product product = productServices.addProduct(productDTO, userPrinciple.getId());

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    private ImageDTO convertMultipartFileToImageDTO(MultipartFile imageFile) {
        ImageDTO imageDTO = new ImageDTO();
        // Tạo tên file ngẫu nhiên bằng UUID và lấy phần mở rộng từ tên file gốc
        String originalFileName = imageFile.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String randomFileName = UUID.randomUUID().toString() + fileExtension;

        imageDTO.setFileName(randomFileName);
        imageDTO.setImageType(imageFile.getContentType());
        return imageDTO;
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("price") BigDecimal price,@RequestParam("quantity") int quantity, @RequestParam("category") String category, @RequestParam("images") List<MultipartFile> images, Authentication authentication) {
        // Lấy thông tin người dùng từ token JWT
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        // Tìm danh mục theo tên
        Category categoryObj = categoryService.findByName(category);

        // Chuyển đổi các file ảnh thành ImageDTO
        List<ImageDTO> imageDTOs = new ArrayList<>();
        if (images != null) {
            for (MultipartFile imageFile : images) {
                ImageDTO imageDTO = convertMultipartFileToImageDTO(imageFile);
                imageDTOs.add(imageDTO);
            }
        }

        // Tạo DTO cho sản phẩm
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setDescription(description);
        productDTO.setPrice(price);
        productDTO.setQuantity(quantity);
        productDTO.setCategory(categoryObj);
        productDTO.setImages(imageDTOs);

        // Cập nhật sản phẩm trong service
        Product updatedProduct = productServices.updateProduct(productId, productDTO);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productServices.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/view/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        ProductDTO productDTO = productServices.getProductById(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }


}
