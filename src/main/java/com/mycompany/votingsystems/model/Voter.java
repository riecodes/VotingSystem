package com.mycompany.votingsystems.model;

import java.util.Arrays;
import java.util.List;

public class Voter extends User {
    private String voterId;
    private boolean hasVoted;
    private String place;  // Voter's registered place
    private boolean isPreHashed;

    // Position-based validation rules
    private static final List<String> NATIONAL_POSITIONS = Arrays.asList(
        "President", "Vice President", "Senator"
    );

    private static final List<String> LOCAL_POSITIONS = Arrays.asList(
        "Governor", "Vice Governor", "Mayor", "Vice Mayor"
    );

    public Voter(String username, String password, String voterId, String place) {
        super(username, password, "VOTER");
        this.voterId = voterId;
        this.hasVoted = false;
        this.place = place;
        this.isPreHashed = false;
    }

    public Voter(String username, String passwordHash, String voterId, String place, boolean isPreHashed) {
        super(username, passwordHash, "VOTER", isPreHashed);
        this.voterId = voterId;
        this.hasVoted = false;
        this.place = place;
        this.isPreHashed = isPreHashed;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * Checks if the voter is eligible to vote for a candidate based on their position and location
     * @param candidate The candidate to check eligibility for
     * @return true if the voter can vote for this candidate, false otherwise
     */
    public boolean canVoteFor(Candidate candidate) {
        String position = candidate.getPosition();
        String candidatePlace = candidate.getPlace();

        // National positions can be voted by anyone
        if (NATIONAL_POSITIONS.contains(position)) {
            return true;
        }

        // Local positions require place match
        if (LOCAL_POSITIONS.contains(position)) {
            return place.equalsIgnoreCase(candidatePlace);
        }

        return false;
    }

    @Override
    public String toString() {
        return "Voter{" +
                "username='" + getUsername() + '\'' +
                ", voterId='" + voterId + '\'' +
                ", place='" + place + '\'' +
                '}';
    }
} 