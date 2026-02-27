package com.quickserve.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.quickserve.model.Notification;
import com.quickserve.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    

    // Get notifications for customer
    @GetMapping("/{customerId}")
    public List<Notification> getNotifications(@PathVariable Long customerId) {
        return notificationService.getCustomerNotifications(customerId);
    }

    // Mark as read
    @PutMapping("/read/{id}")
    public String markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "Notification marked as read";
    }
    @GetMapping("/{customerId}/unread-count")
    public long getUnreadCount(@PathVariable Long customerId) {
        return notificationService.getUnreadCount(customerId);
    }
    @DeleteMapping("/{id}")
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "Notification deleted successfully";
    }
}