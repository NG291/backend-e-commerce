package com.casestudy5.controller.review;

import com.casestudy5.dto.ReviewDTO;
import com.casestudy5.service.review.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    @PostMapping("/product/{productId}/user/{userId}")
    public ResponseEntity<ReviewDTO> addReview(@PathVariable Long productId,
                                               @PathVariable Long userId,
                                               @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO newReview = reviewService.addReview(productId, userId, reviewDTO.getRating(), reviewDTO.getComment());
        return ResponseEntity.ok(newReview);
    }

    @GetMapping("/product-check/{productId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long productId) {
        double averageRating = reviewService.getProductAverageRating(productId);
        return ResponseEntity.ok(averageRating);
    }
}
