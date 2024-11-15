package com.casestudy5.model.entity.image;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageDTO {
    private long id;
    private String imageType;
    private String fileName;
    private MultipartFile file;
}
