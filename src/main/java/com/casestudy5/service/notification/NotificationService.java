package com.casestudy5.service.notification;

import com.casestudy5.model.entity.notification.Notification;
import com.casestudy5.model.entity.notification.NotificationDTO;
import com.casestudy5.repo.INotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private INotificationRepository notificationRepository;

    public List<NotificationDTO> getNotificationsForUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);

        return notifications.stream()
                .map(notification -> new NotificationDTO(notification.getId(), notification.getMessage(), notification.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
