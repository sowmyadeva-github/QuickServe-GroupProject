package com.quickserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.quickserve.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    long countByCustomerIdAndReadStatusFalse(Long customerId);
}