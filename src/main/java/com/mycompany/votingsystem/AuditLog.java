package com.mycompany.votingsystem;

import java.time.LocalDateTime;

public class AuditLog {
    private String action;
    private String user;
    private LocalDateTime timestamp;
    private String details;

    public AuditLog(String action, String user, LocalDateTime timestamp, String details) {
        this.action = action;
        this.user = user;
        this.timestamp = timestamp;
        this.details = details;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
} 