package com.mycompany.votingsystems.model;

import java.util.Objects;

public class Candidate {
    private String candidateId;
    private String name;
    private String party;
    private String position;  // e.g., "President", "Vice President", "Senator", "Representative"
    private String province;
    private String city;
    private int voteCount;
    private boolean isIncumbent;  // Whether the candidate is currently holding the position
    private int termCount;    // Number of terms served (for positions with term limits)

    public Candidate(String candidateId, String name, String party, String position, String province, String city) {
        this.candidateId = candidateId;
        this.name = name;
        this.party = party;
        this.position = position;
        this.province = province;
        this.city = city;
        this.voteCount = 0;
        this.isIncumbent = false;
        this.termCount = 0;
    }

    public void incrementVoteCount() {
        this.voteCount++;
    }

    public void incrementTermCount() {
        this.termCount++;
    }

    public boolean isEligibleForReelection() {
        // Different positions have different term limits in the Philippines
        switch (position.toLowerCase()) {
            case "president":
            case "vice president":
                return termCount < 1;  // One term limit
            case "senator":
                return termCount < 2;  // Two consecutive terms limit
            case "representative":
                return termCount < 3;  // Three consecutive terms limit
            default:
                return true;  // Other positions may have different rules
        }
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isIncumbent() {
        return isIncumbent;
    }

    public void setIncumbent(boolean incumbent) {
        isIncumbent = incumbent;
    }

    public int getTermCount() {
        return termCount;
    }

    public void setTermCount(int termCount) {
        this.termCount = termCount;
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
                ", position='" + position + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", votes=" + voteCount +
                ", isIncumbent=" + isIncumbent +
                ", termCount=" + termCount +
                '}';
    }
} 