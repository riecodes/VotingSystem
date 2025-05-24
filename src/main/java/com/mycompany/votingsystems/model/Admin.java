package com.mycompany.votingsystems.model;

public class Admin extends User {
    private String adminId;

    public Admin(String username, String password, String adminId) {
        super(username, password, "ADMIN");
        this.adminId = adminId;
    }

    public Admin(String username, String passwordHash, String adminId, boolean isPreHashed) {
        super(username, passwordHash, "ADMIN", isPreHashed);
        this.adminId = adminId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "username='" + getUsername() + '\'' +
                ", adminId='" + adminId + '\'' +
                '}';
    }
} 