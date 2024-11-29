package com.casestudy5.controller.review;
import com.casestudy5.model.entity.review.ReviewDTO;
import com.casestudy5.service.review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

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

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProduct(@PathVariable Long productId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByProduct(productId);
        return ResponseEntity.ok(reviews);
    }
}