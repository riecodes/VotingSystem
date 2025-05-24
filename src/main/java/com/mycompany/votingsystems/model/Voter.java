package com.mycompany.votingsystems.model;

import java.util.Arrays;
import java.util.List;

public class Voter extends User {
    private String voterId;
    private boolean hasVoted;
    private String province;  // Voter's registered province
    private String city;      // Voter's registered city
    private boolean isPreHashed;

    // Position-based validation rules
    private static final List<String> NATIONAL_POSITIONS = Arrays.asList(
        "President", "Vice President", "Senator"
    );

    private static final List<String> PROVINCIAL_POSITIONS = Arrays.asList(
        "Governor", "Vice Governor", "Provincial Board Member"
    );

    private static final List<String> CITY_POSITIONS = Arrays.asList(
        "Mayor", "Vice Mayor", "Councilor"
    );

    public Voter(String username, String password, String voterId, String province, String city) {
        super(username, password, "VOTER");
        this.voterId = voterId;
        this.hasVoted = false;
        this.province = province;
        this.city = city;
        this.isPreHashed = false;
    }

    public Voter(String username, String passwordHash, String voterId, String province, String city, boolean isPreHashed) {
        super(username, passwordHash, "VOTER", isPreHashed);
        this.voterId = voterId;
        this.hasVoted = false;
        this.province = province;
        this.city = city;
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

    /**
     * Checks if the voter is eligible to vote for a candidate based on their position and location
     * @param candidate The candidate to check eligibility for
     * @return true if the voter can vote for this candidate, false otherwise
     */
    public boolean canVoteFor(Candidate candidate) {
        String position = candidate.getPosition();
        String candidateProvince = candidate.getProvince();
        String candidateCity = candidate.getCity();

        // National positions can be voted by anyone
        if (NATIONAL_POSITIONS.contains(position)) {
            return true;
        }

        // Provincial positions require province match
        if (PROVINCIAL_POSITIONS.contains(position)) {
            return province.equalsIgnoreCase(candidateProvince);
        }

        // City positions require city match
        if (CITY_POSITIONS.contains(position)) {
            return city.equalsIgnoreCase(candidateCity);
        }

        return false;
    }

    @Override
    public String toString() {
        return "Voter{" +
                "username='" + getUsername() + '\'' +
                ", voterId='" + voterId + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
} 