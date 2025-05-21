package com.mycompany.votingsystems.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.mycompany.votingsystems.VotingSystemManager;
import com.mycompany.votingsystems.model.Candidate;
import com.mycompany.votingsystems.model.User;
import com.mycompany.votingsystems.model.Voter;

public class VoterDashboard extends JFrame {
    private final VotingSystemManager manager;
    private final JTable candidatesTable;
    private final DefaultTableModel candidatesModel;
    private final JLabel statusLabel;

    public VoterDashboard() {
        manager = VotingSystemManager.getInstance();
        setTitle("Voter Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Voting Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Candidates table
        candidatesModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Party"}, 0);
        candidatesTable = new JTable(candidatesModel);
        mainPanel.add(new JScrollPane(candidatesTable), BorderLayout.CENTER);

        // Status label
        statusLabel = new JLabel();
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton voteButton = new JButton("Cast Vote");
        voteButton.addActionListener(e -> handleVote());
        buttonPanel.add(voteButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            manager.logout();
            dispose();
            new LoginScreen().setVisible(true);
        });
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Load initial data
        refreshCandidatesTable();
        updateStatus();
    }

    private void handleVote() {
        int selectedRow = candidatesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a candidate to vote for",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String candidateId = (String) candidatesModel.getValueAt(selectedRow, 0);
        String candidateName = (String) candidatesModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to vote for " + candidateName + "?",
                "Confirm Vote",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (manager.castVote(candidateId)) {
                JOptionPane.showMessageDialog(this,
                        "Your vote has been recorded successfully!",
                        "Vote Cast",
                        JOptionPane.INFORMATION_MESSAGE);
                updateStatus();
            } else {
                JOptionPane.showMessageDialog(this,
                        "You have already cast your vote",
                        "Vote Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshCandidatesTable() {
        candidatesModel.setRowCount(0);
        List<Candidate> candidates = manager.getCandidates();
        for (Candidate candidate : candidates) {
            candidatesModel.addRow(new Object[]{
                    candidate.getCandidateId(),
                    candidate.getName(),
                    candidate.getParty()
            });
        }
    }

    private void updateStatus() {
        User currentUser = manager.getCurrentUser();
        if (currentUser instanceof Voter) {
            Voter voter = (Voter) currentUser;
            if (voter.hasVoted()) {
                statusLabel.setText("You have already cast your vote");
                statusLabel.setForeground(Color.RED);
            } else {
                statusLabel.setText("You have not voted yet");
                statusLabel.setForeground(Color.BLUE);
            }
        }
    }
} 