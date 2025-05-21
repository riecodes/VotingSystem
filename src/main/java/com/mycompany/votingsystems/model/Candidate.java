package com.mycompany.votingsystems.model;

import java.util.Objects;

public class Candidate {
    private String candidateId;
    private String name;
    private String party;
    private int voteCount;

    public Candidate(String candidateId, String name, String party) {
        this.candidateId = candidateId;
        this.name = name;
        this.party = party;
        this.voteCount = 0;
    }

    public void incrementVoteCount() {
        this.voteCount++;
    }

    // Getters and setters
    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return Objects.equals(candidateId, candidate.candidateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidateId);
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "candidateId='" + candidateId + '\'' +
                ", name='" + name + '\'' +
                ", party='" + party + '\'' +
                ", voteCount=" + voteCount +
                '}';
    }
} 