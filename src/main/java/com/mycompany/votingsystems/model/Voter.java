package com.mycompany.votingsystems.model;

public class Voter extends User {
    private String voterId;
    private boolean hasVoted;

    public Voter(String username, String password, String voterId) {
        super(username, password, "VOTER");
        this.voterId = voterId;
        this.hasVoted = false;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public boolean hasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    @Override
    public String toString() {
        return "Voter{" +
                "username='" + getUsername() + '\'' +
                ", voterId='" + voterId + '\'' +
                ", hasVoted=" + hasVoted +
                '}';
    }
} 