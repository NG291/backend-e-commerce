package com.casestudy5.service.review;

import com.casestudy5.dto.ReviewDTO;

public interface IReviewService {
    ReviewDTO addReview(Long productId, Long userId, Integer rating, String comment);
    double getProductAverageRating(Long productId);
}
