package com.mycompany.votingsystem;

public class Voter extends User {
    private String voterID;
    private boolean hasVoted;

    public Voter(String username, String hashedPassword, String voterID) {
        super(username, hashedPassword, "VOTER");
        this.voterID = voterID;
        this.hasVoted = false;
    }

    public String getVoterID() {
        return voterID;
    }

    public void setVoterID(String voterID) {
        this.voterID = voterID;
    }

    public boolean hasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    @Override
    public boolean authenticate(String password) {
        // Implement password hashing check here
        return PasswordUtils.verifyPassword(password, this.hashedPassword);
    }
} 