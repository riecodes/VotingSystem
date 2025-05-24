package com.mycompany.votingsystems.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.mycompany.votingsystems.VotingSystemManager;
import com.mycompany.votingsystems.model.AuditLog;
import com.mycompany.votingsystems.model.Candidate;

public class AdminDashboard extends JFrame {
    private final VotingSystemManager manager;
    private final JTable candidatesTable;
    private final JTable auditLogTable;
    private final DefaultTableModel candidatesModel;
    private final DefaultTableModel auditLogModel;
    
    // Philippine electoral positions
    private static final String[] POSITIONS = {
        "President",
        "Vice President",
        "Senator",
        "Governor",
        "Vice Governor",
        "Mayor",
        "Vice Mayor",
        "Councilor"
    };

    private static final String[] PROVINCES = {
        "Metro Manila",
        "Cebu",
        "Davao",
        "Quezon",
        "Cavite",
        "Rizal",
        "Bulacan",
        "Laguna",
        "Pampanga",
        "Batangas"
    };

    private static final String[] CITIES = {
        "Manila",
        "Quezon City",
        "Makati",
        "Taguig",
        "Pasig",
        "Mandaluyong",
        "Pasay",
        "Muntinlupa",
        "Las Piñas",
        "Parañaque"
    };

    public AdminDashboard() {
        manager = VotingSystemManager.getInstance();
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create main panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Candidates tab
        JPanel candidatesPanel = new JPanel(new BorderLayout());
        candidatesModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Party", "Position", "Province", "City", "Votes"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        candidatesTable = new JTable(candidatesModel);
        candidatesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        candidatesPanel.add(new JScrollPane(candidatesTable), BorderLayout.CENTER);

        JPanel candidatesButtonPanel = new JPanel();
        JButton addCandidateButton = new JButton("Add Candidate");
        JButton updateCandidateButton = new JButton("Update Candidate");
        JButton deleteCandidateButton = new JButton("Delete Candidate");

        addCandidateButton.addActionListener(e -> showAddCandidateDialog());
        updateCandidateButton.addActionListener(e -> showUpdateCandidateDialog());
        deleteCandidateButton.addActionListener(e -> deleteSelectedCandidate());

        candidatesButtonPanel.add(addCandidateButton);
        candidatesButtonPanel.add(updateCandidateButton);
        candidatesButtonPanel.add(deleteCandidateButton);
        candidatesPanel.add(candidatesButtonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Candidates", candidatesPanel);

        // Audit Log tab
        JPanel auditLogPanel = new JPanel(new BorderLayout());
        auditLogModel = new DefaultTableModel(
                new String[]{"Action", "User", "Timestamp", "Details"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        auditLogTable = new JTable(auditLogModel);
        auditLogPanel.add(new JScrollPane(auditLogTable), BorderLayout.CENTER);

        tabbedPane.addTab("Audit Log", auditLogPanel);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            manager.logout();
            dispose();
            new LoginScreen().setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(logoutButton);

        // Add components to frame
        add(tabbedPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load initial data
        refreshCandidatesTable();
        refreshAuditLogTable();
    }

    private void showAddCandidateDialog() {
        JPanel panel = new JPanel(new GridLayout(6, 2));
        JTextField nameField = new JTextField();
        JTextField partyField = new JTextField();
        JComboBox<String> positionCombo = new JComboBox<>(POSITIONS);
        JComboBox<String> provinceCombo = new JComboBox<>(PROVINCES);
        JComboBox<String> cityCombo = new JComboBox<>(CITIES);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Party:"));
        panel.add(partyField);
        panel.add(new JLabel("Position:"));
        panel.add(positionCombo);
        panel.add(new JLabel("Province:"));
        panel.add(provinceCombo);
        panel.add(new JLabel("City:"));
        panel.add(cityCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Candidate",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String party = partyField.getText();
            String position = (String) positionCombo.getSelectedItem();
            String province = (String) provinceCombo.getSelectedItem();
            String city = (String) cityCombo.getSelectedItem();

            if (name.isEmpty() || party.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
                return;
            }

            if (manager.addCandidate(name, party, position, province, city)) {
                JOptionPane.showMessageDialog(this, "Candidate added successfully!");
                refreshCandidatesTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add candidate.");
            }
        }
    }

    private void showUpdateCandidateDialog() {
        int selectedRow = candidatesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a candidate to update",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String candidateId = (String) candidatesModel.getValueAt(selectedRow, 0);
        String currentName = (String) candidatesModel.getValueAt(selectedRow, 1);
        String currentParty = (String) candidatesModel.getValueAt(selectedRow, 2);
        String currentPosition = (String) candidatesModel.getValueAt(selectedRow, 3);
        String currentProvince = (String) candidatesModel.getValueAt(selectedRow, 4);
        String currentCity = (String) candidatesModel.getValueAt(selectedRow, 5);

        JDialog dialog = new JDialog(this, "Update Candidate", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(currentName);
        JTextField partyField = new JTextField(currentParty);
        JComboBox<String> positionCombo = new JComboBox<>(POSITIONS);
        positionCombo.setSelectedItem(currentPosition);
        JComboBox<String> provinceCombo = new JComboBox<>(PROVINCES);
        provinceCombo.setSelectedItem(currentProvince);
        JComboBox<String> cityCombo = new JComboBox<>(CITIES);
        cityCombo.setSelectedItem(currentCity);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Party:"));
        panel.add(partyField);
        panel.add(new JLabel("Position:"));
        panel.add(positionCombo);
        panel.add(new JLabel("Province:"));
        panel.add(provinceCombo);
        panel.add(new JLabel("City:"));
        panel.add(cityCombo);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            String newName = nameField.getText().trim();
            String newParty = partyField.getText().trim();
            String newPosition = (String) positionCombo.getSelectedItem();
            String newProvince = (String) provinceCombo.getSelectedItem();
            String newCity = (String) cityCombo.getSelectedItem();

            if (newName.isEmpty() || newParty.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Please fill in all required fields",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (manager.updateCandidate(candidateId, newName, newParty, newPosition, newProvince, newCity)) {
                JOptionPane.showMessageDialog(this, "Candidate updated successfully!");
                refreshCandidatesTable();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update candidate.");
            }
        });

        panel.add(updateButton);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deleteSelectedCandidate() {
        int selectedRow = candidatesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a candidate to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String candidateId = (String) candidatesModel.getValueAt(selectedRow, 0);
        String candidateName = (String) candidatesModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete candidate " + candidateName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (manager.deleteCandidate(candidateId)) {
                refreshCandidatesTable();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error deleting candidate",
                        "Error",
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
                    candidate.getParty(),
                    candidate.getPosition(),
                    candidate.getProvince(),
                    candidate.getCity(),
                    candidate.getVoteCount()
            });
        }
    }

    private void refreshAuditLogTable() {
        auditLogModel.setRowCount(0);
        List<AuditLog> logs = manager.getAuditLogs();
        for (AuditLog log : logs) {
            auditLogModel.addRow(new Object[]{
                    log.getAction(),
                    log.getUsername(),
                    log.getTimestamp(),
                    log.getDetails()
            });
        }
    }
} 