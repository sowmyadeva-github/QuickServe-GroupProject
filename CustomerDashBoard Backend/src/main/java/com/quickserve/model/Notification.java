package com.quickserve.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private String message;

    private boolean readStatus = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Notification() {}

    public Notification(Long customerId, String message) {
        this.customerId = customerId;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.readStatus = false;
    }

    public Long getId() { return id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isReadStatus() { return readStatus; }
    public void setReadStatus(boolean readStatus) { this.readStatus = readStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}