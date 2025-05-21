package com.mycompany.votingsystem;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingSystemManager {
    private static VotingSystemManager instance;
    private Map<String, User> users;
    private Map<String, Candidate> candidates;
    private List<Vote> votes;
    private List<AuditLog> auditLogs;

    private final String USERS_FILE = "src/main/resources/data/users.csv";
    private final String CANDIDATES_FILE = "src/main/resources/data/candidates.csv";
    private final String VOTES_FILE = "src/main/resources/data/votes.csv";
    private final String AUDITLOG_FILE = "src/main/resources/data/auditlog.csv";

    private VotingSystemManager() {
        users = new HashMap<>();
        candidates = new HashMap<>();
        votes = new ArrayList<>();
        auditLogs = new ArrayList<>();
    }

    public static VotingSystemManager getInstance() {
        if (instance == null) {
            instance = new VotingSystemManager();
        }
        return instance;
    }

    // Load and save methods for users, candidates, votes, and audit logs
    public void loadAllData() throws IOException {
        loadUsers();
        loadCandidates();
        loadVotes();
        loadAuditLogs();
    }

    public void saveAllData() throws IOException {
        saveUsers();
        saveCandidates();
        saveVotes();
        saveAuditLogs();
    }

    private void loadUsers() throws IOException {
        users.clear();
        List<String[]> data = CSVUtils.readCSV(USERS_FILE);
        for (String[] row : data) {
            if (row.length >= 4) {
                String role = row[2];
                if (role.equals("ADMIN")) {
                    users.put(row[0], new Admin(row[0], row[1]));
                } else if (role.equals("VOTER")) {
                    Voter voter = new Voter(row[0], row[1], row[3]);
                    if (row.length > 4) {
                        voter.setHasVoted(Boolean.parseBoolean(row[4]));
                    }
                    users.put(row[0], voter);
                }
            }
        }
    }

    private void saveUsers() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (User user : users.values()) {
            if (user instanceof Admin) {
                data.add(new String[]{user.getUsername(), user.getHashedPassword(), user.getRole(), ""});
            } else if (user instanceof Voter) {
                Voter voter = (Voter) user;
                data.add(new String[]{voter.getUsername(), voter.getHashedPassword(), voter.getRole(), voter.getVoterID(), String.valueOf(voter.hasVoted())});
            }
        }
        CSVUtils.writeCSV(USERS_FILE, data);
    }

    private void loadCandidates() throws IOException {
        candidates.clear();
        List<String[]> data = CSVUtils.readCSV(CANDIDATES_FILE);
        for (String[] row : data) {
            if (row.length >= 3) {
                Candidate candidate = new Candidate(row[0], row[1], row[2]);
                if (row.length > 3) {
                    for (int i = 0; i < Integer.parseInt(row[3]); i++) {
                        candidate.incrementVoteCount();
                    }
                }
                candidates.put(row[0], candidate);
            }
        }
    }

    private void saveCandidates() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (Candidate candidate : candidates.values()) {
            data.add(new String[]{candidate.getCandidateID(), candidate.getName(), candidate.getParty(), String.valueOf(candidate.getVoteCount())});
        }
        CSVUtils.writeCSV(CANDIDATES_FILE, data);
    }

    private void loadVotes() throws IOException {
        votes.clear();
        List<String[]> data = CSVUtils.readCSV(VOTES_FILE);
        for (String[] row : data) {
            if (row.length >= 3) {
                votes.add(new Vote(row[0], row[1], LocalDateTime.parse(row[2])));
            }
        }
    }

    private void saveVotes() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (Vote vote : votes) {
            data.add(new String[]{vote.getVoterID(), vote.getCandidateID(), vote.getTimestamp().toString()});
        }
        CSVUtils.writeCSV(VOTES_FILE, data);
    }

    private void loadAuditLogs() throws IOException {
        auditLogs.clear();
        List<String[]> data = CSVUtils.readCSV(AUDITLOG_FILE);
        for (String[] row : data) {
            if (row.length >= 4) {
                auditLogs.add(new AuditLog(row[0], row[1], LocalDateTime.parse(row[2]), row[3]));
            }
        }
    }

    private void saveAuditLogs() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (AuditLog log : auditLogs) {
            data.add(new String[]{log.getAction(), log.getUser(), log.getTimestamp().toString(), log.getDetails()});
        }
        CSVUtils.writeCSV(AUDITLOG_FILE, data);
    }

    // Registration, authentication, voting, candidate management, and logging methods will be added here.

    public boolean registerVoter(String username, String password, String voterID) throws IOException {
        if (users.containsKey(username)) return false;
        String hashed = PasswordUtils.hashPassword(password);
        Voter voter = new Voter(username, hashed, voterID);
        users.put(username, voter);
        saveUsers();
        logAction("REGISTER_VOTER", username, "Voter registered");
        return true;
    }

    public boolean registerAdmin(String username, String password) throws IOException {
        if (users.containsKey(username)) return false;
        String hashed = PasswordUtils.hashPassword(password);
        Admin admin = new Admin(username, hashed);
        users.put(username, admin);
        saveUsers();
        logAction("REGISTER_ADMIN", username, "Admin registered");
        return true;
    }

    public User authenticateUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.authenticate(password)) {
            logAction("AUTHENTICATE", username, "User authenticated");
            return user;
        }
        logAction("AUTHENTICATE_FAIL", username, "Authentication failed");
        return null;
    }

    public boolean addCandidate(String candidateID, String name, String party) throws IOException {
        if (candidates.containsKey(candidateID)) return false;
        Candidate candidate = new Candidate(candidateID, name, party);
        candidates.put(candidateID, candidate);
        saveCandidates();
        logAction("ADD_CANDIDATE", candidateID, "Candidate added");
        return true;
    }

    public boolean vote(String voterUsername, String candidateID) throws IOException {
        User user = users.get(voterUsername);
        if (!(user instanceof Voter)) return false;
        Voter voter = (Voter) user;
        if (voter.hasVoted()) return false;
        Candidate candidate = candidates.get(candidateID);
        if (candidate == null) return false;
        candidate.incrementVoteCount();
        voter.setHasVoted(true);
        votes.add(new Vote(voter.getVoterID(), candidateID, java.time.LocalDateTime.now()));
        saveCandidates();
        saveUsers();
        saveVotes();
        logAction("VOTE", voterUsername, "Voted for " + candidateID);
        return true;
    }

    public void logAction(String action, String user, String details) {
        AuditLog log = new AuditLog(action, user, java.time.LocalDateTime.now(), details);
        auditLogs.add(log);
        try {
            saveAuditLogs();
        } catch (IOException e) {
            // Handle logging error (optional: print stack trace)
        }
    }

    public Map<String, Candidate> getCandidates() {
        return candidates;
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }
} 