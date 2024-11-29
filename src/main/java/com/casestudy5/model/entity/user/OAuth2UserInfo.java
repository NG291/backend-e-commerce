package com.casestudy5.model.entity.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Data
@Setter
public class OAuth2UserInfo {
    private String email;
    private String name;
    private String picture;
}