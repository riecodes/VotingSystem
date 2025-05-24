package com.mycompany.votingsystems.gui;

import com.mycompany.votingsystems.VotingSystemManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JComboBox<String> roleComboBox;
    private final VotingSystemManager manager;

    // Philippine provinces
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

    // Map of cities by province
    private static final Map<String, String[]> CITIES_BY_PROVINCE = new HashMap<>();
    
    static {
        // Metro Manila cities
        CITIES_BY_PROVINCE.put("Metro Manila", new String[]{
            "Manila",
            "Quezon City",
            "Makati",
            "Taguig",
            "Pasig",
            "Pasay",
            "Mandaluyong",
            "San Juan"
        });
        
        // Other major cities
        CITIES_BY_PROVINCE.put("Cebu", new String[]{"Cebu City"});
        CITIES_BY_PROVINCE.put("Davao", new String[]{"Davao City"});
        CITIES_BY_PROVINCE.put("Iloilo", new String[]{"Iloilo City"});
        CITIES_BY_PROVINCE.put("Zamboanga", new String[]{"Zamboanga City"});
        CITIES_BY_PROVINCE.put("Cagayan", new String[]{"Cagayan de Oro"});
        CITIES_BY_PROVINCE.put("Pampanga", new String[]{"Angeles City"});
        CITIES_BY_PROVINCE.put("Cavite", new String[]{"Bacoor", "Imus"});
        CITIES_BY_PROVINCE.put("Palawan", new String[]{"Puerto Princesa"});
        CITIES_BY_PROVINCE.put("Albay", new String[]{"Legazpi"});
        CITIES_BY_PROVINCE.put("Leyte", new String[]{"Tacloban"});
        CITIES_BY_PROVINCE.put("South Cotabato", new String[]{"General Santos"});
        CITIES_BY_PROVINCE.put("Ilocos Norte", new String[]{"Laoag"});
        CITIES_BY_PROVINCE.put("Ilocos Sur", new String[]{"Vigan"});
    }

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
        JPanel panel = new JPanel(new GridLayout(5, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> provinceCombo = new JComboBox<>(PROVINCES);
        JComboBox<String> cityCombo = new JComboBox<>(CITIES);

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Province:"));
        panel.add(provinceCombo);
        panel.add(new JLabel("City:"));
        panel.add(cityCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Register New Voter",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String province = (String) provinceCombo.getSelectedItem();
            String city = (String) cityCombo.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
                return;
            }

            if (manager.registerVoter(username, password, province, city)) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Username may already exist.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginScreen().setVisible(true);
        });
    }
} 