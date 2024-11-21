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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    @Value("${upload.image}")
    private String uploadPathImage;
    @Override
    @Transactional
    public Product addProduct(ProductDTO productDTO, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setUser(user);
        product.setCategory(productDTO.getCategory());

        product = productRepository.save(product);

        // Lưu các ảnh vào thư mục và cơ sở dữ liệu
        for (ImageDTO imageDTO : productDTO.getImages()) {

            Image existingImage = imageRepository.findByFileName(imageDTO.getFileName());

            if (existingImage == null) {
                if (imageDTO.getFile() != null && !imageDTO.getFile().isEmpty()) {

                    // Đường dẫn thư mục lưu trữ ảnh
                    String filePath = Paths.get(uploadPathImage, imageDTO.getFileName()).toString();
                    File file = new File(filePath);

                    try {
                        imageDTO.getFile().transferTo(file);
                    } catch (IOException e) {
                        throw new RuntimeException("Error saving file: " + imageDTO.getFileName(), e);
                    }

                    Image image = new Image();
                    image.setImageType(imageDTO.getImageType());
                    image.setFileName(imageDTO.getFileName());
                    image.setProduct(product);
                    imageRepository.save(image);
                }
            } else {
                existingImage.setProduct(product);
                imageRepository.save(existingImage);
            }
        }

        return product;
    }

    @Transactional
    @Override
    public Product updateProduct(Long productId, ProductDTO productDTO) {
        // Tìm sản phẩm trong cơ sở dữ liệu theo productId
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setCategory(productDTO.getCategory());

        List<Image> oldImages = imageRepository.findByProductId(existingProduct.getId());

        // Xóa hết ảnh cũ trong cơ sở dữ liệu và xóa file trong thư mục
        for (Image oldImage : oldImages) {
            // Tạo đường dẫn tới file ảnh cũ trong thư mục
            String filePath = Paths.get(uploadPathImage, oldImage.getFileName()).toString();
            File file = new File(filePath);

            // Kiểm tra và xóa file nếu tồn tại
            if (file.exists()) {
                if (!file.delete()) {
                    throw new RuntimeException("Error deleting file: " + filePath);
                }
            }
        }

        // Xóa hết ảnh cũ
        imageRepository.deleteAllByProductId(existingProduct.getId());


        // Cập nhật ảnh nếu có thay đổi
        List<Image> images = new ArrayList<>();
        for (ImageDTO imageDTO : productDTO.getImages()) {
            Image image = new Image();
            image.setImageType(imageDTO.getImageType());
            image.setFileName(imageDTO.getFileName());
            image.setProduct(existingProduct);

            // Lưu ảnh vào thư mục trên máy chủ
            try {
                // Đường dẫn tới thư mục lưu ảnh
                String filePath = Paths.get(uploadPathImage, imageDTO.getFileName()).toString();
                File file = new File(filePath);

                // Kiểm tra nếu thư mục chưa tồn tại, tạo mới
                File parentDir = file.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                }

                // Lưu file vào thư mục
                imageDTO.getFile().transferTo(file); // Lưu file vào đường dẫn đã chỉ định
            } catch (IOException e) {
                throw new RuntimeException("Error saving image to server: " + imageDTO.getFileName(), e);
            }

            images.add(image);
        }

        imageRepository.saveAll(images);


        return productRepository.save(existingProduct);
    }

    @Override
    public boolean deleteProduct(Long productId) {
        try {
            // Kiểm tra xem sản phẩm có tồn tại hay không
            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

            // Nếu tồn tại, xóa sản phẩm
            productRepository.delete(product);
            return true;
        } catch (Exception e) {
            // Nếu có lỗi, trả về false
            return false;
        }
    }




    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ImageDTO convertImageToDTO(Image image) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(image.getId());
        imageDTO.setFileName(image.getFileName());
        imageDTO.setImageType(image.getImageType());
        return imageDTO;
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        ProductDTO productDTO = convertToProductDTO(product);

        return productDTO;
    }

    private ProductDTO convertToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setCategory(product.getCategory());
        List<ImageDTO> imageDTOs = product.getImages().stream()
                .map(this::convertImageToDTO)
                .collect(Collectors.toList());
        productDTO.setImages(imageDTOs);

        return productDTO;
    }

    public List<ProductDTO> getProductsBySeller(Long userId) throws Exception {
        List<Product> products = productRepository.findByUserId(userId);

        if (products.isEmpty()) {
            throw new Exception("No products found for this seller.");
        }

        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return productDTOs;
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setCategory(product.getCategory());
        productDTO.setImages(
                product.getImages().stream()
                        .map(this::convertImageToDTO)
                        .collect(Collectors.toList())
        );
        return productDTO;
    }

}



