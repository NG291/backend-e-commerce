package com.casestudy5.model.entity.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateUser {
    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private LocalDate birthDate;
    private String avatar;
}
