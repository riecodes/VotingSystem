package com.mycompany.votingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML private Label welcomeLabel;
    @FXML private Button voteButton;
    @FXML private Button resultsButton;
    @FXML private Button candidateButton;
    @FXML private Button auditLogButton;
    @FXML private Button logoutButton;

    @FXML
    public void initialize() {
        User user = AppState.getCurrentUser();
        welcomeLabel.setText("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        if (user.getRole().equals("ADMIN")) {
            voteButton.setVisible(false);
        } else if (user.getRole().equals("VOTER")) {
            candidateButton.setVisible(false);
            auditLogButton.setVisible(false);
        }
    }

    @FXML
    private void goToVote() throws Exception {
        App.setRoot("voting");
    }

    @FXML
    private void goToResults() throws Exception {
        App.setRoot("results");
    }

    @FXML
    private void goToCandidate() throws Exception {
        App.setRoot("admin");
    }

    @FXML
    private void goToAuditLog() throws Exception {
        App.setRoot("auditlog");
    }

    @FXML
    private void logout() throws Exception {
        AppState.setCurrentUser(null);
        App.setRoot("login");
    }
} 