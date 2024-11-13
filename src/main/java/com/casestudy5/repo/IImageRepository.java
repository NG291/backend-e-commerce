package com.casestudy5.repo;

import com.casestudy5.model.entity.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IImageRepository extends JpaRepository<Image, Integer> {
}
