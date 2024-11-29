package com.casestudy5.model.entity.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
@Data
@Builder
public class FormUpdateUser {
    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private LocalDate birthDate;
    private MultipartFile avatar;
}
