package com.casestudy5.service.review;

import com.casestudy5.model.entity.review.ReviewDTO;

import java.util.List;

public interface IReviewService {
    ReviewDTO addReview(Long productId, Long userId, Integer rating, String comment);
    double getProductAverageRating(Long productId);
    List<ReviewDTO> getReviewsByProduct(Long productId);
}