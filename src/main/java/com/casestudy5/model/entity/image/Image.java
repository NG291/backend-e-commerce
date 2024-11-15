    package com.casestudy5.model.entity.image;

    import com.casestudy5.model.entity.product.Product;
    import com.fasterxml.jackson.annotation.JsonBackReference;
    import jakarta.persistence.*;
    import lombok.Data;

    @Data
    @Entity
    @Table(name = "images")
    public class Image {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JsonBackReference
        @JoinColumn(name = "product_id")
        private Product product;

        @Column(name = "image_type")
        private String imageType;

        @Column(name = "file_name")
        private String fileName;

    }

