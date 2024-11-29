package com.casestudy5.model.entity.notification;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class NotificationDTO {
    private Long id;
    private String message;
    private LocalDateTime createdAt;
    public NotificationDTO(Long id, String message, LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
    }
}

