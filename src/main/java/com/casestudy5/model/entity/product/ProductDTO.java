package com.casestudy5.model.entity.product;

import com.casestudy5.model.entity.image.ImageDTO;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
@Setter
@Getter
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Category category;
    private List<ImageDTO> images;
}
