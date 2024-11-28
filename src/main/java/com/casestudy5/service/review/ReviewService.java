package com.casestudy5.service.review;

import com.casestudy5.dto.ReviewDTO;
import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.review.Review;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.repo.IProductRepository;
import com.casestudy5.repo.IReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        user.setId(userId); // Assuming userId is valid; ensure proper validation in real apps.

        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setProduct(product);
        review.setUser(user);

        Review savedReview = reviewRepository.save(review);

        // Map the saved review to DTO
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(savedReview.getId());
        reviewDTO.setRating(savedReview.getRating());
        reviewDTO.setComment(savedReview.getComment());
        reviewDTO.setProductId(productId);
        reviewDTO.setUserId(userId);

        return reviewDTO;
    }

    @Override
    public double getProductAverageRating(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return product.getAverageRating();
    }
}
