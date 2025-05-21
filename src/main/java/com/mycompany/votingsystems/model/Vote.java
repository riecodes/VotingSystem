package com.mycompany.votingsystems.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Vote {
    private String voterId;
    private String candidateId;
    private LocalDateTime timestamp;

    public Vote(String voterId, String candidateId) {
        this.voterId = voterId;
        this.candidateId = candidateId;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(voterId, vote.voterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voterId);
    }

    @Override
    public String toString() {
        return "Vote{" +
                "voterId='" + voterId + '\'' +
                ", candidateId='" + candidateId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
} 