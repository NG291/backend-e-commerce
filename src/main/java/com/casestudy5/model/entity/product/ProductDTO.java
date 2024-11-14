package com.casestudy5.model.entity.product;

import com.casestudy5.model.entity.image.ImageDTO;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;
@Data
@Setter
@Getter
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private Category category;
    private List<ImageDTO> images;
}
