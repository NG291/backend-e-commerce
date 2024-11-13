package com.casestudy5.model.entity.image;

import lombok.Data;

@Data
public class ImageDTO {
    private Integer id; // Thêm trường id
    private String imageType;
    private String fileName;

    public String getUrl() {
        return "/images/" + fileName;
    }
}
