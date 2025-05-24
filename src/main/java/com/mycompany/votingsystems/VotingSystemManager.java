package com.mycompany.votingsystems;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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

    private static final String DATA_DIR = System.getProperty("user.dir") + "/data";
    private static final String USERS_FILE = DATA_DIR + "/users.csv";
    private static final String CANDIDATES_FILE = DATA_DIR + "/candidates.csv";
    private static final String VOTES_FILE = DATA_DIR + "/votes.csv";
    private static final String AUDIT_LOG_FILE = DATA_DIR + "/audit_log.csv";

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
        Files.createDirectories(Paths.get(DATA_DIR));
        System.out.println("Data directory created at: " + DATA_DIR);
    }

    private void loadUsers() throws IOException {
        System.out.println("Loading users from: " + USERS_FILE);
        if (!Files.exists(Paths.get(USERS_FILE))) {
            System.out.println("Users file does not exist, creating default admin");
            // Create default admin user if no users exist
            Admin admin = new Admin("admin", "admin", "ADMIN001");
            users.put(admin.getUsername(), admin);
            saveUsers();
            return;
        }

        try (Reader reader = new FileReader(USERS_FILE);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            for (CSVRecord record : csvParser) {
                String username = record.get(0);
                String role = record.get(1);
                String id = record.get(2);
                String passwordHash = record.get(3);
                System.out.println("Loading user: " + username + ", role: " + role + ", id: " + id);

                User user;
                if ("ADMIN".equals(role)) {
                    // For admin, always use the default password
                    user = new Admin("admin", "admin", "ADMIN001");
                } else {
                    // For voters, use the stored password hash and location info
                    String district = record.size() > 4 ? record.get(4) : "";
                    String province = record.size() > 5 ? record.get(5) : "";
                    String city = record.size() > 6 ? record.get(6) : "";
                    user = new Voter(username, passwordHash, id, district, province, city, true);
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
                String position = record.get(3);
                String district = record.size() > 4 ? record.get(4) : null;
                int voteCount = Integer.parseInt(record.get(record.size() - 1));

                Candidate candidate;
                if (district != null) {
                    candidate = new Candidate(candidateId, name, party, position, district);
                } else {
                    candidate = new Candidate(candidateId, name, party, position);
                }
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
        System.out.println("Saving users to: " + USERS_FILE);
        try (Writer writer = new FileWriter(USERS_FILE);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (User user : users.values()) {
                if (user instanceof Admin admin) {
                    csvPrinter.printRecord(user.getUsername(), user.getRole(), admin.getAdminId(), user.getPasswordHash());
                    System.out.println("Saved admin: " + user.getUsername());
                } else if (user instanceof Voter voter) {
                    csvPrinter.printRecord(
                        user.getUsername(),
                        user.getRole(),
                        voter.getVoterId(),
                        user.getPasswordHash(),
                        voter.getDistrict(),
                        voter.getProvince(),
                        voter.getCity()
                    );
                    System.out.println("Saved voter: " + user.getUsername());
                }
            }
        }
    }

    private void saveCandidates() throws IOException {
        try (Writer writer = new FileWriter(CANDIDATES_FILE);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (Candidate candidate : candidates.values()) {
                csvPrinter.printRecord(
                    candidate.getCandidateId(),
                    candidate.getName(),
                    candidate.getParty(),
                    candidate.getPosition(),
                    candidate.getDistrict(),
                    candidate.getVoteCount()
                );
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
        System.out.println("Attempting login for: " + username + " with role: " + role);
        User user = users.get(username);
        if (user != null) {
            System.out.println("User found, checking password and role");
            // For admin, always check against "admin" password
            if ("ADMIN".equals(role)) {
                if (username.equals("admin") && password.equals("admin")) {
                    currentUser = user;
                    logAction("LOGIN", username, "Admin logged in successfully");
                    return true;
                }
            } else {
                // For voters, check the stored password hash
                if (user.authenticate(password) && user.getRole().equalsIgnoreCase(role)) {
                    currentUser = user;
                    logAction("LOGIN", username, "Voter logged in successfully");
                    return true;
                }
            }
        }
        System.out.println("Login failed for: " + username);
        logAction("LOGIN_FAILED", username, "Failed login attempt");
        return false;
    }

    public void logout() {
        if (currentUser != null) {
            logAction("LOGOUT", currentUser.getUsername(), "User logged out");
            currentUser = null;
        }
    }

    private String generateVoterId() {
        int nextNumber = (int) users.values().stream()
                .filter(user -> user instanceof Voter)
                .count() + 1;
        return String.format("VOTER%03d", nextNumber);
    }

    public boolean registerVoter(String username, String password, String province, String city) {
        if (users.containsKey(username)) {
            return false;
        }

        String voterId = generateVoterId();
        Voter voter = new Voter(username, password, voterId, province, city);
        users.put(username, voter);
        try {
            saveUsers();
            logAction("REGISTER", username, "New voter registered with ID: " + voterId);
            return true;
        } catch (IOException e) {
            logError("Error saving user data", e);
            return false;
        }
    }

    private String generateCandidateId() {
        int nextNumber = candidates.size() + 1;
        return String.format("CAND%03d", nextNumber);
    }

    public boolean addCandidate(String name, String party, String position, String province, String city) {
        String candidateId = generateCandidateId();
        if (candidates.containsKey(candidateId)) {
            return false;
        }

        Candidate candidate = new Candidate(candidateId, name, party, position, province, city);
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

    public boolean updateCandidate(String candidateId, String name, String party, String position, String province, String city) {
        Candidate candidate = candidates.get(candidateId);
        if (candidate == null) {
            return false;
        }

        candidate.setName(name);
        candidate.setParty(party);
        candidate.setPosition(position);
        candidate.setProvince(province);
        candidate.setCity(city);
        try {
            saveCandidates();
            logAction("UPDATE_CANDIDATE", currentUser.getUsername(), 
                    "Updated candidate: " + name + " (ID: " + candidateId + ")");
            return true;
        } catch (IOException e) {
            logError("Error saving candidate data", e);
            return false;
        }
    }

    public boolean deleteCandidate(String candidateId) {
        Candidate candidate = candidates.remove(candidateId);
        if (candidate == null) {
            return false;
        }

        try {
            saveCandidates();
            logAction("DELETE_CANDIDATE", currentUser.getUsername(), 
                    "Deleted candidate: " + candidate.getName() + " (ID: " + candidateId + ")");
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

        // Check if voter is eligible to vote for this candidate
        if (!voter.canVoteFor(candidate)) {
            logAction("VOTE_REJECTED", currentUser.getUsername(), 
                "Attempted to vote for ineligible candidate: " + candidate.getName() + 
                " (Position: " + candidate.getPosition() + 
                ", District: " + candidate.getDistrict() + ")");
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
            logAction("VOTE", currentUser.getUsername(), 
                "Voted for candidate: " + candidate.getName() + 
                " (Position: " + candidate.getPosition() + 
                ", District: " + candidate.getDistrict() + ")");
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