package com.casestudy5.service.product;




import com.casestudy5.model.entity.image.Image;
import com.casestudy5.model.entity.image.ImageDTO;


import com.casestudy5.repo.IImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private IImageRepository imageRepository;

    @Override
    public ImageDTO createImage(ImageDTO imageDTO) {

        Optional<Image> existingImage = imageRepository.findByFileNameAndImageType(imageDTO.getFileName(), imageDTO.getImageType());

        if (existingImage.isPresent()) {
            return convertToDTO(existingImage.get());
        }

        Image image = new Image();
        image.setFileName(imageDTO.getFileName());
        image.setImageType(imageDTO.getImageType());

        image = imageRepository.save(image);

        return convertToDTO(image);
    }
    private ImageDTO convertToDTO(Image image) {
        ImageDTO dto = new ImageDTO();
        dto.setId(image.getId());
        dto.setFileName(image.getFileName());
        dto.setImageType(image.getImageType());
        return dto;
    }
}
