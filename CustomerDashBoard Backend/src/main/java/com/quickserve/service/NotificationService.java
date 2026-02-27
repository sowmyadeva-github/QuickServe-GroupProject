package com.quickserve.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.quickserve.model.Notification;
import com.quickserve.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(Long customerId, String message) {
        Notification notification = new Notification(customerId, message);
        notificationRepository.save(notification);
    }

    public List<Notification> getCustomerNotifications(Long customerId) {
        return notificationRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }
    public long getUnreadCount(Long customerId) {
        return notificationRepository
                .countByCustomerIdAndReadStatusFalse(customerId);
    }
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
    
}