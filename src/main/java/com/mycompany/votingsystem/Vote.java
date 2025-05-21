package com.mycompany.votingsystem;

import java.time.LocalDateTime;

public class Vote {
    private String voterID;
    private String candidateID;
    private LocalDateTime timestamp;

    public Vote(String voterID, String candidateID, LocalDateTime timestamp) {
        this.voterID = voterID;
        this.candidateID = candidateID;
        this.timestamp = timestamp;
    }

    public String getVoterID() {
        return voterID;
    }

    public void setVoterID(String voterID) {
        this.voterID = voterID;
    }

    public String getCandidateID() {
        return candidateID;
    }

    public void setCandidateID(String candidateID) {
        this.candidateID = candidateID;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
} 