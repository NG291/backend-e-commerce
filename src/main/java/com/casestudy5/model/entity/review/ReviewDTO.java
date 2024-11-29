package com.casestudy5.model.entity.review;


import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;
    private Integer rating;
    private String comment;
    private Long userId;
    private Long productId;
    private String userName;

    private String createdAt; // Thêm trường ngày đánh giá
}