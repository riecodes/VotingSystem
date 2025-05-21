package com.mycompany.votingsystems;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import com.mycompany.votingsystems.model.Admin;
import com.mycompany.votingsystems.model.AuditLog;
import com.mycompany.votingsystems.model.Candidate;
import com.mycompany.votingsystems.model.User;
import com.mycompany.votingsystems.model.Vote;
import com.mycompany.votingsystems.model.Voter;

public class VotingSystemManager {
    private static VotingSystemManager instance;
    private final Map<String, User> users;
    private final Map<String, Candidate> candidates;
    private final List<Vote> votes;
    private final List<AuditLog> auditLogs;
    private User currentUser;

    private static final String USERS_FILE = "data/users.csv";
    private static final String CANDIDATES_FILE = "data/candidates.csv";
    private static final String VOTES_FILE = "data/votes.csv";
    private static final String AUDIT_LOG_FILE = "data/audit_log.csv";

    private VotingSystemManager() {
        users = new HashMap<>();
        candidates = new HashMap<>();
        votes = new ArrayList<>();
        auditLogs = new ArrayList<>();
        loadData();
    }

    public static synchronized VotingSystemManager getInstance() {
        if (instance == null) {
            instance = new VotingSystemManager();
        }
        return instance;
    }

    private void loadData() {
        try {
            createDataDirectory();
            loadUsers();
            loadCandidates();
            loadVotes();
            loadAuditLogs();
        } catch (IOException e) {
            logError("Error loading data", e);
        }
    }

    private void createDataDirectory() throws IOException {
        Files.createDirectories(Paths.get("data"));
    }

    private void loadUsers() throws IOException {
        if (!Files.exists(Paths.get(USERS_FILE))) {
            // Create default admin user if no users exist
            Admin admin = new Admin("admin", "admin123", "ADMIN001");
            users.put(admin.getUsername(), admin);
            saveUsers();
            return;
        }

        try (Reader reader = new FileReader(USERS_FILE);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            for (CSVRecord record : csvParser) {
                String username = record.get(0);
                String password = record.get(1);
                String role = record.get(2);
                String id = record.get(3);

                User user;
                if ("ADMIN".equals(role)) {
                    user = new Admin(username, password, id);
                } else {
                    user = new Voter(username, password, id);
                }
                users.put(username, user);
            }
        }
    }

    private void loadCandidates() throws IOException {
        if (!Files.exists(Paths.get(CANDIDATES_FILE))) {
            return;
        }

        try (Reader reader = new FileReader(CANDIDATES_FILE);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            for (CSVRecord record : csvParser) {
                String candidateId = record.get(0);
                String name = record.get(1);
                String party = record.get(2);
                int voteCount = Integer.parseInt(record.get(3));

                Candidate candidate = new Candidate(candidateId, name, party);
                candidate.setVoteCount(voteCount);
                candidates.put(candidateId, candidate);
            }
        }
    }

    private void loadVotes() throws IOException {
        if (!Files.exists(Paths.get(VOTES_FILE))) {
            return;
        }

        try (Reader reader = new FileReader(VOTES_FILE);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            for (CSVRecord record : csvParser) {
                String voterId = record.get(0);
                String candidateId = record.get(1);
                votes.add(new Vote(voterId, candidateId));
            }
        }
    }

    private void loadAuditLogs() throws IOException {
        if (!Files.exists(Paths.get(AUDIT_LOG_FILE))) {
            return;
        }

        try (Reader reader = new FileReader(AUDIT_LOG_FILE);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            for (CSVRecord record : csvParser) {
                String action = record.get(0);
                String username = record.get(1);
                String details = record.get(2);
                auditLogs.add(new AuditLog(action, username, details));
            }
        }
    }

    private void saveUsers() throws IOException {
        try (Writer writer = new FileWriter(USERS_FILE);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (User user : users.values()) {
                if (user instanceof Admin admin) {
                    csvPrinter.printRecord(user.getUsername(), "admin123", user.getRole(), admin.getAdminId());
                } else if (user instanceof Voter voter) {
                    csvPrinter.printRecord(user.getUsername(), "password", user.getRole(), voter.getVoterId());
                }
            }
        }
    }

    private void saveCandidates() throws IOException {
        try (Writer writer = new FileWriter(CANDIDATES_FILE);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (Candidate candidate : candidates.values()) {
                csvPrinter.printRecord(candidate.getCandidateId(), candidate.getName(),
                        candidate.getParty(), candidate.getVoteCount());
            }
        }
    }

    private void saveVotes() throws IOException {
        try (Writer writer = new FileWriter(VOTES_FILE);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (Vote vote : votes) {
                csvPrinter.printRecord(vote.getVoterId(), vote.getCandidateId());
            }
        }
    }

    private void saveAuditLogs() throws IOException {
        try (Writer writer = new FileWriter(AUDIT_LOG_FILE);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (AuditLog log : auditLogs) {
                csvPrinter.printRecord(log.getAction(), log.getUsername(), log.getDetails());
            }
        }
    }

    public boolean login(String username, String password, String role) {
        User user = users.get(username);
        if (user != null && user.authenticate(password) && user.getRole().equals(role)) {
            currentUser = user;
            logAction("LOGIN", username, "User logged in successfully");
            return true;
        }
        logAction("LOGIN_FAILED", username, "Failed login attempt");
        return false;
    }

    public void logout() {
        if (currentUser != null) {
            logAction("LOGOUT", currentUser.getUsername(), "User logged out");
            currentUser = null;
        }
    }

    public boolean registerVoter(String username, String password, String voterId) {
        if (users.containsKey(username)) {
            return false;
        }

        Voter voter = new Voter(username, password, voterId);
        users.put(username, voter);
        try {
            saveUsers();
            logAction("REGISTER", username, "New voter registered");
            return true;
        } catch (IOException e) {
            logError("Error saving user data", e);
            return false;
        }
    }

    public boolean addCandidate(String candidateId, String name, String party) {
        if (candidates.containsKey(candidateId)) {
            return false;
        }

        Candidate candidate = new Candidate(candidateId, name, party);
        candidates.put(candidateId, candidate);
        try {
            saveCandidates();
            logAction("ADD_CANDIDATE", currentUser.getUsername(), "Added candidate: " + name);
            return true;
        } catch (IOException e) {
            logError("Error saving candidate data", e);
            return false;
        }
    }

    public boolean castVote(String candidateId) {
        if (!(currentUser instanceof Voter voter)) {
            return false;
        }

        if (voter.hasVoted()) {
            return false;
        }

        Candidate candidate = candidates.get(candidateId);
        if (candidate == null) {
            return false;
        }

        Vote vote = new Vote(voter.getVoterId(), candidateId);
        votes.add(vote);
        candidate.incrementVoteCount();
        voter.setHasVoted(true);

        try {
            saveVotes();
            saveCandidates();
            saveUsers();
            logAction("VOTE", currentUser.getUsername(), "Voted for candidate: " + candidate.getName());
            return true;
        } catch (IOException e) {
            logError("Error saving vote data", e);
            return false;
        }
    }

    public List<Candidate> getCandidates() {
        return new ArrayList<>(candidates.values());
    }

    public List<AuditLog> getAuditLogs() {
        return new ArrayList<>(auditLogs);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private void logAction(String action, String username, String details) {
        AuditLog log = new AuditLog(action, username, details);
        auditLogs.add(log);
        try {
            saveAuditLogs();
        } catch (IOException e) {
            logError("Error saving audit log", e);
        }
    }

    private void logError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }
} 