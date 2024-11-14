package com.casestudy5.repo;

import com.casestudy5.model.entity.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByFileNameAndImageType(String fileName, String imageType);

    Image findByFileName(String fileName);

    List<Image> findByProductId(Long productId);

    List<Image> findByImageType(String imageType);

    int deleteAllByProductId(Long productId);
}
