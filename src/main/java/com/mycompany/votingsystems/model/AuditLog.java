package com.mycompany.votingsystems.model;

import java.time.LocalDateTime;

public class AuditLog {
    private String action;
    private String username;
    private LocalDateTime timestamp;
    private String details;

    public AuditLog(String action, String username, String details) {
        this.action = action;
        this.username = username;
        this.timestamp = LocalDateTime.now();
        this.details = details;
    }

    // Getters and setters
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "action='" + action + '\'' +
                ", username='" + username + '\'' +
                ", timestamp=" + timestamp +
                ", details='" + details + '\'' +
                '}';
    }
} 