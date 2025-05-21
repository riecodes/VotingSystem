package com.mycompany.votingsystem;

public class Candidate {
    private String candidateID;
    private String name;
    private String party;
    private int voteCount;

    public Candidate(String candidateID, String name, String party) {
        this.candidateID = candidateID;
        this.name = name;
        this.party = party;
        this.voteCount = 0;
    }

    public String getCandidateID() {
        return candidateID;
    }

    public void setCandidateID(String candidateID) {
        this.candidateID = candidateID;
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

    public void incrementVoteCount() {
        this.voteCount++;
    }
} 