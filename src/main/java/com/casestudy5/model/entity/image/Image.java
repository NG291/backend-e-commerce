    package com.casestudy5.model.entity.image;

    import com.casestudy5.model.entity.product.Product;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.springframework.data.jpa.domain.support.AuditingEntityListener;

    @Data
    @Entity
    public class Image {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer  id;

        @ManyToOne
        @JoinColumn(name = "product_id")
        private Product product;

        @Column(name = "image_type")
        private String imageType;

        @Column(name = "file_name")
        private String fileName;

        public String getUrl() {
            return "/images/" + fileName;
        }

    }

