package com.mycompany.votingsystems.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
                new String[]{"ID", "Name", "Party", "Votes"}, 0);
        candidatesTable = new JTable(candidatesModel);
        candidatesPanel.add(new JScrollPane(candidatesTable), BorderLayout.CENTER);

        JPanel candidatesButtonPanel = new JPanel();
        JButton addCandidateButton = new JButton("Add Candidate");
        addCandidateButton.addActionListener(e -> showAddCandidateDialog());
        candidatesButtonPanel.add(addCandidateButton);
        candidatesPanel.add(candidatesButtonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Candidates", candidatesPanel);

        // Audit Log tab
        JPanel auditLogPanel = new JPanel(new BorderLayout());
        auditLogModel = new DefaultTableModel(
                new String[]{"Action", "User", "Timestamp", "Details"}, 0);
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
        JDialog dialog = new JDialog(this, "Add Candidate", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField partyField = new JTextField();

        panel.add(new JLabel("Candidate ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Party:"));
        panel.add(partyField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String id = idField.getText();
            String name = nameField.getText();
            String party = partyField.getText();

            if (id.isEmpty() || name.isEmpty() || party.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Please fill in all fields",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (manager.addCandidate(id, name, party)) {
                refreshCandidatesTable();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Candidate ID already exists",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(addButton);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void refreshCandidatesTable() {
        candidatesModel.setRowCount(0);
        List<Candidate> candidates = manager.getCandidates();
        for (Candidate candidate : candidates) {
            candidatesModel.addRow(new Object[]{
                    candidate.getCandidateId(),
                    candidate.getName(),
                    candidate.getParty(),
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