package com.mycompany.votingsystems.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.mycompany.votingsystems.VotingSystemManager;
import com.mycompany.votingsystems.model.Candidate;
import com.mycompany.votingsystems.model.Voter;

public class VoterDashboard extends JFrame {
    private final VotingSystemManager manager;
    private final JTable candidatesTable;
    private final DefaultTableModel candidatesModel;

    public VoterDashboard() {
        manager = VotingSystemManager.getInstance();
        setTitle("Voter Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create candidates table
        candidatesModel = new DefaultTableModel(
                new String[]{"Name", "Party", "Position", "Place"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        candidatesTable = new JTable(candidatesModel);
        candidatesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainPanel.add(new JScrollPane(candidatesTable), BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton voteButton = new JButton("Vote for Selected Candidate");
        JButton logoutButton = new JButton("Logout");

        voteButton.addActionListener(e -> voteForSelectedCandidate());
        logoutButton.addActionListener(e -> {
            manager.logout();
            dispose();
            new LoginScreen().setVisible(true);
        });

        buttonPanel.add(voteButton);
        buttonPanel.add(logoutButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Load initial data
        refreshCandidatesTable();
    }

    private void refreshCandidatesTable() {
        candidatesModel.setRowCount(0);
        List<Candidate> candidates = manager.getCandidates();
        Voter voter = (Voter) manager.getCurrentUser();
        String voterPlace = voter.getPlace();

        for (Candidate candidate : candidates) {
            // Show all national candidates (President, Vice President, Senator)
            // For local positions, only show candidates from voter's place
            if (isNationalPosition(candidate.getPosition()) || 
                (candidate.getPlace() != null && candidate.getPlace().equals(voterPlace))) {
                // Don't show candidates for positions the voter has already voted for
                if (!manager.hasVotedForPosition(candidate.getPosition())) {
                    candidatesModel.addRow(new Object[]{
                        candidate.getName(),
                        candidate.getParty(),
                        candidate.getPosition(),
                        candidate.getPlace()
                    });
                }
            }
        }
    }

    private boolean isNationalPosition(String position) {
        return position.equals("President") || 
               position.equals("Vice President") || 
               position.equals("Senator");
    }

    private void voteForSelectedCandidate() {
        int selectedRow = candidatesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a candidate to vote for",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String candidateName = (String) candidatesModel.getValueAt(selectedRow, 0);
        String candidatePosition = (String) candidatesModel.getValueAt(selectedRow, 2);
        String candidatePlace = (String) candidatesModel.getValueAt(selectedRow, 3);

        // Find the candidate ID from the manager
        String candidateId = null;
        for (Candidate candidate : manager.getCandidates()) {
            if (candidate.getName().equals(candidateName) && 
                candidate.getPosition().equals(candidatePosition) && 
                ((candidate.getPlace() == null && candidatePlace == null) || 
                 (candidate.getPlace() != null && candidate.getPlace().equals(candidatePlace)))) {
                candidateId = candidate.getCandidateId();
                break;
            }
        }

        if (candidateId == null) {
            JOptionPane.showMessageDialog(this,
                    "Error finding candidate",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to vote for " + candidateName + " for " + candidatePosition + "?",
                "Confirm Vote",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (manager.castVote(candidateId)) {
                JOptionPane.showMessageDialog(this, "Vote cast successfully!");
                refreshCandidatesTable(); // Refresh to remove voted positions
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error casting vote. You may have already voted for this position.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 