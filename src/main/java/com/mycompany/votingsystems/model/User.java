package com.mycompany.votingsystems.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public abstract class User {
    private String username;
    private String passwordHash;
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.role = role;
    }

    public User(String username, String passwordHash, String role, boolean isPreHashed) {
        this.username = username;
        this.passwordHash = isPreHashed ? passwordHash : hashPassword(passwordHash);
        this.role = role;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            String hashedPassword = hexString.toString();
            System.out.println("Hashed password for " + username + ": " + hashedPassword);
            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public boolean authenticate(String password) {
        String inputHash = hashPassword(password);
        boolean matches = passwordHash.equals(inputHash);
        System.out.println("Authentication for " + username + ":");
        System.out.println("  Stored hash: " + passwordHash);
        System.out.println("  Input hash:  " + inputHash);
        System.out.println("  Matches:     " + matches);
        return matches;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
} 