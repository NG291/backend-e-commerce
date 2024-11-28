package com.casestudy5.repo;

import com.casestudy5.model.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IReviewRepository extends JpaRepository<Review,Long> {
}