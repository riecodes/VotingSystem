package com.mycompany.votingsystem;

public class Admin extends User {
    public Admin(String username, String hashedPassword) {
        super(username, hashedPassword, "ADMIN");
    }

    @Override
    public boolean authenticate(String password) {
        // Implement password hashing check here
        return PasswordUtils.verifyPassword(password, this.hashedPassword);
    }

    // Methods for managing candidates and viewing results will be added here
} 