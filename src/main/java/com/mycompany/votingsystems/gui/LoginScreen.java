package com.mycompany.votingsystems.gui;

import com.mycompany.votingsystems.VotingSystemManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JComboBox<String> roleComboBox;
    private final VotingSystemManager manager;

    public LoginScreen() {
        manager = VotingSystemManager.getInstance();
        setTitle("Voting System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Voting System Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Username field
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernamePanel.add(new JLabel("Username:"));
        usernameField = new JTextField(20);
        usernamePanel.add(usernameField);
        mainPanel.add(usernamePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Password field
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Role selection
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rolePanel.add(new JLabel("Role:"));
        String[] roles = {"Voter", "Admin"};
        roleComboBox = new JComboBox<>(roles);
        rolePanel.add(roleComboBox);
        mainPanel.add(rolePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        mainPanel.add(loginButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Register button
        JButton registerButton = new JButton("Register as Voter");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegistrationDialog();
            }
        });
        mainPanel.add(registerButton);

        add(mainPanel);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (manager.login(username, password, role)) {
            dispose();
            if ("Admin".equals(role)) {
                new AdminDashboard().setVisible(true);
            } else {
                new VoterDashboard().setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username, password, or role",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRegistrationDialog() {
        JDialog dialog = new JDialog(this, "Register as Voter", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField voterIdField = new JTextField();

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Voter ID:"));
        panel.add(voterIdField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String voterId = voterIdField.getText();

            if (username.isEmpty() || password.isEmpty() || voterId.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Please fill in all fields",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (manager.registerVoter(username, password, voterId)) {
                JOptionPane.showMessageDialog(dialog,
                        "Registration successful! You can now login.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Username already exists",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(registerButton);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginScreen().setVisible(true);
        });
    }
} 