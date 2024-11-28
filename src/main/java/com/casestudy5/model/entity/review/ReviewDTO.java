package com.casestudy5.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;
    private Integer rating;
    private String comment;
    private Long userId;
    private Long productId;
}
