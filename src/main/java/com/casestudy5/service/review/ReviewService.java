package com.casestudy5.service.review;

import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.review.Review;
import com.casestudy5.model.entity.review.ReviewDTO;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.repo.IProductRepository;
import com.casestudy5.repo.IReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IReviewRepository reviewRepository;

    @Override
    @Transactional
    public ReviewDTO addReview(Long productId, Long userId, Integer rating, String comment) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        User user = new User();
        user.setId(userId);

        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setProduct(product);
        review.setUser(user);

        Review savedReview = reviewRepository.save(review);

        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(savedReview.getId());
        reviewDTO.setRating(savedReview.getRating());
        reviewDTO.setComment(savedReview.getComment());
        reviewDTO.setProductId(productId);
        reviewDTO.setUserId(userId);

        reviewDTO.setUserName(user.getName()); // Assuming User has a 'name' field

        return reviewDTO;
    }

    @Override
    public double getProductAverageRating(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return product.getAverageRating();
    }

    @Override
    public List<ReviewDTO> getReviewsByProduct(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        return reviews.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private ReviewDTO mapToDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setProductId(review.getProduct().getId()); // Assuming Product has an ID
        reviewDTO.setUserId(review.getUser().getId()); // Assuming User has an ID
        reviewDTO.setUserName(review.getUser().getName());
        reviewDTO.setCreatedAt(review.getCreatedAt().toString()); // Chuyển ngày giờ thành chuỗi
        return reviewDTO;
    }
}